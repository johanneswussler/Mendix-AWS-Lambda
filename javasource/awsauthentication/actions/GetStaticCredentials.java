// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package awsauthentication.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import awsauthentication.impl.MxLogger;
import awsauthentication.proxies.Credentials;

public class GetStaticCredentials extends CustomJavaAction<IMendixObject>
{
	private java.lang.String accessKeyID;
	private java.lang.String secretAccessKey;

	public GetStaticCredentials(IContext context, java.lang.String accessKeyID, java.lang.String secretAccessKey)
	{
		super(context);
		this.accessKeyID = accessKeyID;
		this.secretAccessKey = secretAccessKey;
	}

	@java.lang.Override
	public IMendixObject executeAction() throws Exception
	{
		// BEGIN USER CODE
		Credentials staticCredentials = new Credentials(getContext());
		staticCredentials.setAccessKeyId(accessKeyID);
		staticCredentials.setSecretAccessKey(secretAccessKey);
		staticCredentials.commit();
		return  staticCredentials.getMendixObject();
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "GetStaticCredentials";
	}

	// BEGIN EXTRA CODE
	
	// Unused suppressed as it is not needed at the moment. But will be there if ever needed
	@SuppressWarnings("unused")
	private static final MxLogger LOGGER = new MxLogger(GetStaticCredentials.class);
	// END EXTRA CODE
}
