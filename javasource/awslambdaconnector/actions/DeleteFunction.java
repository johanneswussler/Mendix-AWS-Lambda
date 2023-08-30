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
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.DeleteFunctionRequest;
import software.amazon.awssdk.services.lambda.model.LambdaException;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class DeleteFunction extends CustomJavaAction<java.lang.Void>
{
	private java.lang.String FunctionName;
	private java.lang.String Region;
	private IMendixObject __Credentials;
	private awsauthentication.proxies.Credentials Credentials;

	public DeleteFunction(IContext context, java.lang.String FunctionName, java.lang.String Region, IMendixObject Credentials)
	{
		super(context);
		this.FunctionName = FunctionName;
		this.Region = Region;
		this.__Credentials = Credentials;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.Credentials = this.__Credentials == null ? null : awsauthentication.proxies.Credentials.initialize(getContext(), __Credentials);

		// BEGIN USER CODE
		final ILogNode LOGGER = Core.getLogger("AWSLambdaConnector");
		
		try {
			LambdaClient lambdaClient = AWSLambdaClient.getLambdaClient(getContext(), Credentials, Region);
			
			DeleteFunctionRequest deleteFunctionRequest = DeleteFunctionRequest.builder()
					.functionName(FunctionName)
					.build();
			
			lambdaClient.deleteFunction(deleteFunctionRequest);
		} catch (LambdaException e) {
			LOGGER.error("Exception in Java Client Code, Failed to Invoke Function "+e.getMessage());
			throw new MendixRuntimeException(e);
		}
		
		return null;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "DeleteFunction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}