package com.bevis.social.providers.google;

import com.bevis.social.core.Connection;
import com.bevis.social.core.dto.AccessGrant;
import com.bevis.social.core.dto.ConnectionKey;
import com.bevis.social.core.dto.UserProfile;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleConnection implements Connection {
    private AccessGrant accessGrant;
    private ConnectionKey connectionKey;
    private String appId;
    private String appSecret;
    private String imageUrl;
    private String displayName;

    public GoogleConnection(AccessGrant accessGrant, ConnectionKey connectionKey, String appId, String appSecret) {
        this.accessGrant = accessGrant;
        this.connectionKey = connectionKey;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    @Override
    public ConnectionKey getKey() {
        return connectionKey;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public UserProfile fetchUserProfile() {
        try {
            String accessToken = accessGrant.getAccessToken();
            String clientId = appId;
            String clientSecret = appSecret;
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            HttpTransport httpTransport  = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(jsonFactory)
                    .setClientSecrets(clientId, clientSecret)
                    .build()
                    .setAccessToken(accessToken);
            Oauth2 oauth2Client = new Oauth2.Builder(httpTransport, jsonFactory, credential).build();

            //TODO Add idToken instead of access_token see https://developers.google.com/identity/sign-in/android/backend-auth

            // Retrieve user profile
            Userinfo userinfo = oauth2Client.userinfo().get().execute();
            String id = userinfo.getId();
            String name = userinfo.getName();
            this.displayName = name;
            String firstName = userinfo.getGivenName();
            String lastName = userinfo.getFamilyName();
            String email = userinfo.getEmail();
            String username = userinfo.getId();
            this.imageUrl = userinfo.getPicture();
            this.connectionKey = new ConnectionKey(connectionKey.getProviderId(), id);
            return new UserProfile(id, name, firstName, lastName, email, username);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error");
        }
    }

}
