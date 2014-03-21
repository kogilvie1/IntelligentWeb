package mjk.twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mjk.model.JsonUserOperation;
import mjk.model.TweeterModel;
import mjk.model.UserModel;
import mjk.model.VenuesModel;
import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;

public class FindTweetStreamingApi {
	public ArrayList<String> arraystr;
	public FindTweetStreamingApi(Twitter twitter,final List<VenuesModel>vms) throws Exception{
//        if (args.length < 1) {
//            System.out.println("Usage: java twitter4j.examples.PrintFilterStream [follow(comma separated numerical user ids)] [track(comma separated filter terms)]");
//            System.exit(-1);
//        }
		StatusListener list = new StatusListener()
		{
			 //	@Override public void onStatus(Status status) {
			//	System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			//	}
			@Override
			public void onException(Exception ex) {
				// TODO Auto-generated method stub
				ex.printStackTrace();
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				// TODO Auto-generated method stub
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}
			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses)
			{
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}
			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				// TODO Auto-generated method stub
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				// TODO Auto-generated method stub
				System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onStatus(twitter4j.Status status) {
				// TODO Auto-generated method stub
			
				ArrayList<UserModel> usrs=new ArrayList();
				try {
						status.getUser();
						TweeterModel usertweet=new TweeterModel();
						UserModel usermodel=new UserModel();
						usermodel.setdescription(status.getUser().getDescription());
						usermodel.setlocation(status.getUser().getLocation());
						usermodel.setname(status.getUser().getName());
						usermodel.setprofileurl(status.getUser().getProfileImageURL());
			    		usrs.add(usermodel);
			    		usertweet.settweetcontext(status.getText());
			    		usertweet.setuserhandle(status.getUser().getScreenName());

					}
				
				catch (Exception te) {
				     te.printStackTrace();
				             System.out.println("Failed to search tweets:" +te.getMessage());
				             System.exit(-1);
				        }
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userinfo", usrs);
				JsonUserOperation ujson=new JsonUserOperation();
			//JsonVenuesOperation vjson=new JsonVenuesOperation();
				map.put("venues:", vms);
				String testjson=ujson.JsonGenerate((HashMap<String, Object>) map);
//				arraystr.add(testjson);
				System.out.println(testjson);
			}
		};
        TwitterStreamFactory factory=new TwitterStreamFactory(twitter.getConfiguration());
        TwitterStream twitterStream = factory.getInstance();
        twitterStream.addListener(list);
        for (int i=0;i<vms.size()-1;i++){
        	double la1=(vms.get(i).getLatitude());
        	double la2=(vms.get(i).getLatitude())+4;
        	double lo1=(vms.get(i).getLongtitude());
        	double lo2=(vms.get(i).getLongtitude())+4;
        	
        double[][] coordinate ={{lo1,la1},{lo2,la2}};
        System.out.println(coordinate);
        FilterQuery fq=new FilterQuery();
//        String[] track = null;
//        fq.track(track);
    	fq.locations(coordinate);
    	twitterStream.filter(fq);
        }
        }
            
        
//        double[][] coordinate ={{d,c},{f,e}};
// sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
	
    
	}

	


