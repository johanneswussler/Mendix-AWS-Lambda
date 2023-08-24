package awsauthentication.impl;


import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;

import awsauthentication.proxies.Credentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.StsClientBuilder;

public class AuthCredentialsProvider {
	private static final MxLogger LOGGER = new MxLogger(AuthCredentialsProvider.class);
	
	
	public static synchronized StsClient getStsClient(IContext context, Credentials credentials, String region) throws CoreException {
		LOGGER.info("Creating client for " + StsClient.SERVICE_NAME + " in Region (" + region + ").");
		Region awsRegion = Region.AWS_GLOBAL;
		if(null != region && !"".equals(region)) {
			awsRegion = Region.of(region);
		}

		CredentialsProvider credentialsProvider = getCredentialsProvider(context, credentials);
		StsClientBuilder builder = StsClient.builder()
				.credentialsProvider(credentialsProvider.getAwsCredentialsProvider())
				.httpClientBuilder(ApacheHttpClient.builder())
				.region(awsRegion);		
		StsClient newClient = builder.build();
		return newClient;
	}

	
	public static void setCredentialProvider(final AwsClientBuilder<?, ?> awsClientBuilder, final IContext context,
			final Credentials credentials, final String awsHeaderValue) throws CoreException {
		
		CredentialsProvider credentialsProvider = AuthCredentialsProvider.getCredentialsProvider(context, credentials);
		
		AwsCredentialsProvider awsCredentialsProvider = credentialsProvider.getAwsCredentialsProvider();
		
		awsClientBuilder.credentialsProvider(awsCredentialsProvider)
						.overrideConfiguration(ClientOverrideConfiguration.builder()
								.putHeader("User-Agent", awsHeaderValue)
								.build());
	}
	

	public static CredentialsProvider getCredentialsProvider(IContext context, Credentials credentials) {
		switch (credentials.getProvider()) {
			case _STATIC: return new StaticCredentialsProvider(context, credentials);
			case _SESSION: return new SessionCredentialsProvider(context, credentials);
		}

		throw new IllegalStateException("Unimplemented credentials provider:" + credentials.getProvider().getCaption());
	}
}
