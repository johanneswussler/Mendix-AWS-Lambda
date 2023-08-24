package awslambdaconnector.impl;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.MendixRuntimeException;
import com.mendix.systemwideinterfaces.core.IContext;

import awsauthentication.proxies.Credentials;
import software.amazon.awssdk.services.lambda.LambdaClient;
import awsauthentication.impl.AuthCredentialsProvider;
import awsauthentication.impl.CredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.regions.Region;

public class AWSLambdaClient {
	//TODO: Set logger name
		public final static ILogNode LOGGER = Core.getLogger("AWSLambdaConnector");
		public static LambdaClient getLambdaClient(IContext context, Credentials credentials, String regionAsString) throws CoreException {
			Region region = Region.of(regionAsString);
			CredentialsProvider credentialsProvider = AuthCredentialsProvider.getCredentialsProvider(context, credentials);
			

			LambdaClient newClient = null;
			try {
				AwsCredentialsProvider awsCredentialsProvider = credentialsProvider.getAwsCredentialsProvider();
				
				
				newClient = LambdaClient.builder()
			            .region(region)
			            .credentialsProvider(awsCredentialsProvider)
			            .build();
						
			}
			catch (SdkClientException e) {
				LOGGER.error("Exception in Java Client Code, Failed to Create Lambda Client "+e.getMessage());
				throw new MendixRuntimeException(e);
			}
			catch(SdkServiceException e)
			{
				LOGGER.error("Error Response from AWS Service, Failed to Create Lambda Client "+e.getMessage());
				throw new MendixRuntimeException(e);
			}
			catch (Exception e) 
			{
				LOGGER.error("Exception while creating Lambda Client "+e.getMessage());
				throw new MendixRuntimeException(e);
			}
			return newClient;
		}
}
