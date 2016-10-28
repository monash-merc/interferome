/*
 * Copyright (c) 2010-2011, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.monash.merc.wsclient.ldapws;

import edu.monash.merc.dto.LDAPUser;
import edu.monash.merc.exception.WSException;
import edu.monash.merc.util.ssl.EasyIgnoreSSLProtocolSocketFactory;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by simonyu on 2/06/2014.
 */
public class LdapWSClient {
    private WSConfig wsConfig;

    public LdapWSClient() {
    }

    public LdapWSClient(WSConfig wsConfig) {
        this.wsConfig = wsConfig;
    }

    public void setWsConfig(WSConfig wsConfig) {
        this.wsConfig = wsConfig;
    }

    public boolean login(String userName, String password) {
        this.validateWSConfig();
        this.validateLoginParameters(userName, password);
        StringBuilder wsURL = new StringBuilder();
        wsURL.append(this.wsConfig.getLdapAuthenServiceHost()).append(":");
        wsURL.append(this.wsConfig.getLdapAuthenServicePort()).append("/");
        wsURL.append("login/");
        NameValuePair[] postParams = {new NameValuePair("username", userName), new NameValuePair("password", password)};
        return executeLdapUserLogin(wsURL.toString(), postParams);
    }

    public LDAPUser verifyLdapUser(String userName, String password) {
        this.validateWSConfig();
        this.validateLoginParameters(userName, password);
        StringBuilder wsURL = new StringBuilder();
        wsURL.append(this.wsConfig.getLdapAuthenServiceHost()).append(":");
        wsURL.append(this.wsConfig.getLdapAuthenServicePort()).append("/");
        wsURL.append("verify/");
        NameValuePair[] postParams = {new NameValuePair("username", userName), new NameValuePair("password", password)};
        return executeVerifyLdapUser(wsURL.toString(), postParams);
    }

    public LDAPUser lookup(String cnOrEmail) {
        this.validateWSConfig();
        this.validateLookupParameters(cnOrEmail);
        StringBuilder getURL = new StringBuilder();
        //server name
        getURL.append(this.wsConfig.getLdapAuthenServiceHost()).append(":");
        //server port
        getURL.append(this.wsConfig.getLdapAuthenServicePort()).append("/");
        //if lookup based on email
        if (StringUtils.contains(cnOrEmail, "@")) {
            getURL.append("email/").append(cnOrEmail);
        } else {
            getURL.append("cn/").append(cnOrEmail);
        }
        String encodedGetUrl = null;
        try {
            encodedGetUrl = URIUtil.encodePath(getURL.toString());
        } catch (Exception ex) {
            throw new WSException(ex);
        }
        return executeGetMethod(encodedGetUrl);
    }

    public LDAPUser executeGetMethod(String getMethodUrl) {
        HttpClient client = new HttpClient();
        if (this.wsConfig.isIgnoreCertError() && this.wsConfig.getLdapAuthenServiceHost().startsWith("https://")) {
            Protocol easyhttps = new Protocol("https", new EasyIgnoreSSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", easyhttps);
        }

        GetMethod getMethod = new GetMethod(getMethodUrl);
        BufferedReader br = null;
        StringBuffer strBuf = new StringBuffer();
        try {
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                br = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream()));
                String readLine;
                while (((readLine = br.readLine()) != null)) {
                    strBuf.append(readLine);
                }
                return populateLdapUserResponse(strBuf.toString());
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new WSException(ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception be) {
                    // ignore whatever
                }
            }
            getMethod.releaseConnection();
        }
    }

    public boolean executeLdapUserLogin(String postMethodURL, NameValuePair[] params) {
        HttpClient client = new HttpClient();

        if (this.wsConfig.isIgnoreCertError() && this.wsConfig.getLdapAuthenServiceHost().startsWith("https://")) {
            Protocol easyhttps = new Protocol("https", new EasyIgnoreSSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", easyhttps);
        }

        PostMethod method = new PostMethod(postMethodURL);
        BufferedReader br = null;
        StringBuffer strBuf = new StringBuffer();
        try {
            //set the post params
            method.setRequestBody(params);
            //execute the post method
            int statusCode = client.executeMethod(method);
            //check the response code
            if (statusCode == HttpStatus.SC_OK) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine;
                while (((readLine = br.readLine()) != null)) {
                    strBuf.append(readLine);
                }
                //populate the response json values
                return populateLoginResponse(strBuf.toString());
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new WSException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception be) {
                    // ignore whatever
                }
            }
            method.releaseConnection();
        }
    }

    public LDAPUser executeVerifyLdapUser(String postMethodURL, NameValuePair[] params) {

        HttpClient client = new HttpClient();

        if (this.wsConfig.isIgnoreCertError() && this.wsConfig.getLdapAuthenServiceHost().startsWith("https://")) {
            Protocol easyhttps = new Protocol("https", new EasyIgnoreSSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", easyhttps);
        }

        PostMethod method = new PostMethod(postMethodURL);
        BufferedReader br = null;
        StringBuffer strBuf = new StringBuffer();
        try {
            //set the post params
            method.setRequestBody(params);
            //execute the post method
            int statusCode = client.executeMethod(method);
            //check the response code
            if (statusCode == HttpStatus.SC_OK) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine;
                while (((readLine = br.readLine()) != null)) {
                    strBuf.append(readLine);
                }
                //populate the response json values
                return populateLdapUserResponse(strBuf.toString());
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new WSException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception be) {
                    // ignore whatever
                }
            }
            method.releaseConnection();
        }
    }

    private boolean populateLoginResponse(String jsonStr) {
        JSONObject obj = new JSONObject();
        JSONObject jsonObject = obj.fromObject(jsonStr);
        return jsonObject.getBoolean("success");
    }

    private LDAPUser populateLdapUserResponse(String jsonStr) {
        JSONObject obj = new JSONObject();

        JSONObject jsonObject = obj.fromObject(jsonStr);
        String uid = jsonObject.getString("id");
        String title = jsonObject.getString("title");
        String firstName = jsonObject.getString("firstName");
        String surname = jsonObject.getString("surname");
        String displayName = jsonObject.getString("displayName");
        String email = jsonObject.getString("mail");
        LDAPUser ldapRemoteUser = new LDAPUser();
        ldapRemoteUser.setUid(uid);
        ldapRemoteUser.setTitle(title);
        ldapRemoteUser.setFirstName(firstName);
        ldapRemoteUser.setLastName(surname);
        ldapRemoteUser.setDisplayName(displayName);
        ldapRemoteUser.setMail(email);
        return ldapRemoteUser;
    }

    private void validateLoginParameters(String userName, String password) {
        StringBuffer errorMsg = new StringBuffer();

        if (StringUtils.isBlank(userName)) {
            errorMsg.append("The user name must be provided.\n");
        }
        if (StringUtils.isBlank(password)) {
            errorMsg.append("The user password must be provided.\n");
        }
        // if we have error messages, throw the exception
        if (errorMsg.length() != 0) {
            throw new WSException(errorMsg.toString());
        }
    }

    private void validateLookupParameters(String cnOrEmail) {
        StringBuffer errorMsg = new StringBuffer();

        if (StringUtils.isBlank(cnOrEmail)) {
            errorMsg.append("The user full name or email must be provided.\n");
        }

        // if we have error messages, throw the exception
        if (errorMsg.length() != 0) {
            throw new WSException(errorMsg.toString());
        }
    }


    private void validateWSConfig() {
        StringBuffer errorMsg = new StringBuffer();
        if (StringUtils.isBlank(this.wsConfig.getLdapAuthenServiceHost())) {
            errorMsg.append("The host name of the LDAP service has not been provided.\n");
        }
        if (this.wsConfig.getLdapAuthenServicePort() == 0) {
            errorMsg.append("The port of the LDAP service has not been provided.\n");
        }
        // if we have error messages, throw the exception
        if (errorMsg.length() != 0) {
            throw new WSException(errorMsg.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        WSConfig wsConfig = new WSConfig();
        wsConfig.setIgnoreCertError(true);
        wsConfig.setLdapAuthenServiceHost("http://118.138.234.124");
        wsConfig.setLdapAuthenServicePort(80);

        LdapWSClient wsClient = new LdapWSClient(wsConfig);
        LDAPUser ldapUser = wsClient.verifyLdapUser("xiyu", "qqq");
        if (ldapUser != null)
            System.out.println(" --- verify  ldap user : " + ldapUser.getDisplayName() + " email: " + ldapUser.getMail());
        System.out.println("login: " + wsClient.login("xiyu", "qqqq"));
        //for email
        System.out.println(wsClient.lookup("xiaoming.yu@monash.edu").getDisplayName());
        //for user name
        System.out.println(wsClient.lookup("Simon Yu").getMail());
    }
}
