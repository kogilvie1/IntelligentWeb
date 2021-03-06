package mjk.twitter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.foyt.foursquare.api.FoursquareApiException;
import mjk.foursquare.ConnectWithTwitter;
import mjk.model.DatabaseConnection;
import mjk.model.JsonUserOperation;
import mjk.model.TweeterModel;
import mjk.model.UserModel;
import mjk.model.VenuesModel;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.User;
public class FindLocationStreamingApi {
	private String shorturl;
	private DatabaseConnection dc;
	private List<UserModel> users = new ArrayList<UserModel>();
	private List<VenuesModel>vms=new ArrayList<VenuesModel>();
	private VenuesModel vm;
	private List<TweeterModel> tweets;
    private Status tweet ;
	

	public FindLocationStreamingApi(String args[]) throws Exception{
		
		        if (args.length < 1) {
		            System.out.println("Usage: java twitter4j.examples.PrintFilterStream [follow(comma separated numerical user ids)] [track(comma separated filter terms)]");
		            System.exit(-1);
		        }
		        StatusListener listener = new StatusListener() {
					@Override
		            public void onStatus(Status status) {
		                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()+"-"+status.getUser().getLocation());
		                User user = status.getUser();
		    	  		UserModel usertweet=new UserModel();
		    	  	
		    		  	if(status.getURLEntities()!=null)
		    		  		{
		    		  			URLEntity[] urls = status.getURLEntities();
		    		  			for(URLEntity url : urls){
		    		  				shorturl=url.getURL();
		    		  				ConnectWithTwitter square=new ConnectWithTwitter();
		    		  				try {
										vm=square.ConnectTwitter(shorturl);
									} catch (FoursquareApiException | SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		    		  				usertweet.setdescription(user.getDescription());
		    		  				usertweet.setlocation(user.getLocation());
		    		  				usertweet.setname(user.getName());
		    		  				usertweet.setprofileurl(user.getProfileImageURL());
		    		  				vms.add(vm);
		    		  				users.add(usertweet);
		    				    	  Map<String, Object> map = new HashMap<String, Object>();
		    				      	JsonUserOperation ujson=new JsonUserOperation();
	    				      		map.put("venues:", vms);
		    				      	String testjson=ujson.JsonGenerate((HashMap<String, Object>) map);
		    				      	System.out.println(testjson);
				  		        }
		 		  		}else{};
		    	  
		    	  }
		    	  

		            @Override
		            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
		            }

		            @Override
		            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
		            }

		            @Override
		            public void onScrubGeo(long userId, long upToStatusId) {
		                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
		            }

		            @Override
		            public void onStallWarning(StallWarning warning) {
		                System.out.println("Got stall warning:" + warning);
		            }

		            @Override
		            public void onException(Exception ex) {
		                ex.printStackTrace();
		            }
		        };
		        InitConnectionTwitter tt = new InitConnectionTwitter();
				Twitter twitterConnection=null;
				twitterConnection= tt.init();
		TwitterStreamFactory factory=new TwitterStreamFactory(twitterConnection.getConfiguration());
	    TwitterStream twitterStream = factory.getInstance();
	    twitterStream.addListener(listener);
	    ArrayList<Long> follow = new ArrayList<Long>();
        ArrayList<String> track = new ArrayList<String>();
        for (String arg : args) {
            if (isNumericalArgument(arg)) {
                for (String id : arg.split(",")) {
                    follow.add(Long.parseLong(id));
                }
            } else {
                track.addAll(Arrays.asList(arg.split(",")));
            }
        }
        long[] followArray = new long[follow.size()];
        for (int i = 0; i < follow.size(); i++) {
            followArray[i] = follow.get(i);
        }
        String[] trackArray = track.toArray(new String[track.size()]);
        twitterStream.filter(new FilterQuery(0, followArray, trackArray));
	    // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
       
    	
	}

	private static boolean isNumericalArgument(String argument) {
	        String args[] = argument.split(",");
	        boolean isNumericalArgument = true;
	        for (String arg : args) {
	            try {
	                Integer.parseInt(arg);
	            } catch (NumberFormatException nfe) {
	                isNumericalArgument = false;
	                break;
	            }
	        }
	        return isNumericalArgument;
	    };
}
