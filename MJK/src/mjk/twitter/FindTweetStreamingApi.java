package mjk.twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mjk.model.VenuesModel;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class FindTweetStreamingApi {
	public FindTweetStreamingApi(Twitter twitter,List<VenuesModel>vms) throws Exception{
		StatusListener list = new StatusListener()
		{
			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}
			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses)
			{
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
			public void onStatus(twitter4j.Status status) {
				System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			}
		};
        TwitterStreamFactory factory=new TwitterStreamFactory(twitter.getConfiguration());
        TwitterStream twitterStream = factory.getInstance();
        twitterStream.addListener(list);
         double[][] coordinate = new double[vms.size()][vms.size()];
        for(int i=0;i<vms.size();i++){  
        	coordinate= 
        }
            }
   FilterQuery fq=new FilterQuery();
	fq.locations(coordinate);
	twitterStream.filter(fq);
	}

	}


