// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package awslambdaconnector.actions;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.MendixRuntimeException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import awslambdaconnector.impl.AWSLambdaClient;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class InvokeFunction extends CustomJavaAction<java.lang.String>
{
	private java.lang.String FunctionName;
	private java.lang.String Payload;
	private IMendixObject __Credentials;
	private awsauthentication.proxies.Credentials Credentials;
	private java.lang.String Region;
	private java.lang.Boolean InvokeAsync;

	public InvokeFunction(IContext context, java.lang.String FunctionName, java.lang.String Payload, IMendixObject Credentials, java.lang.String Region, java.lang.Boolean InvokeAsync)
	{
		super(context);
		this.FunctionName = FunctionName;
		this.Payload = Payload;
		this.__Credentials = Credentials;
		this.Region = Region;
		this.InvokeAsync = InvokeAsync;
	}

	@java.lang.Override
	public java.lang.String executeAction() throws Exception
	{
		this.Credentials = this.__Credentials == null ? null : awsauthentication.proxies.Credentials.initialize(getContext(), __Credentials);

		// BEGIN USER CODE
		final ILogNode LOGGER = Core.getLogger("AWSLambdaConnector");

		LambdaClient lambdaClient = AWSLambdaClient.getLambdaClient(getContext(), Credentials, Region);
		InvokeResponse res = null ;
		
		InvocationType invocType = null;
		
		if(InvokeAsync) {
			invocType = InvocationType.EVENT;
		} else {
			invocType = InvocationType.REQUEST_RESPONSE;
		}

		if(Payload != null && Payload != "") {
		        try {
		            // Need a SdkBytes instance for the payload.
		          
		            SdkBytes JSONPayload = SdkBytes.fromUtf8String(Payload) ;

		            // Setup an InvokeRequest.
		            InvokeRequest request = InvokeRequest.builder()
		                .functionName(FunctionName)
		                .payload(JSONPayload)
		                .invocationType(invocType)
		                .build();

		            res = lambdaClient.invoke(request);
		            String responseValue = res.payload().asUtf8String() ;
		            
		            return responseValue;

		        } catch(LambdaException e) {
		        	LOGGER.error("Exception in Java Client Code, Failed to Invoke Function "+e.getMessage());
					throw new MendixRuntimeException(e);
		        }
		} else {
			InvokeRequest request = InvokeRequest.builder()
                .functionName(FunctionName)
                .invocationType(invocType)
                .build();

            res = lambdaClient.invoke(request);
            String responseValue = res.payload().asUtf8String() ;
            
            return responseValue;
		}

		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "InvokeFunction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
