package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.api.Route;
import com.spotify.oauth2.api.TokenManager;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class PlaylistApi {

    @Step
    public static Response post(Playlist requestPlaylist) {
        return RestResource.post(Route.USERS + "/" + ConfigLoader.getInstance().getUserId() + Route.PLAYLISTS, TokenManager.getToken(), requestPlaylist);
    }

    public static Response post(String token, Playlist requestPlaylist) {
        return RestResource.post(Route.USERS +  "/" + ConfigLoader.getInstance().getUserId() + Route.PLAYLISTS, token, requestPlaylist);
    }

    public static Response get(String playlistId) {
        return RestResource.get(Route.PLAYLISTS + "/" + playlistId, TokenManager.getToken());
    }

    public static Response update(Playlist requestPlaylist, String playlistId) {
        return RestResource.update(requestPlaylist, Route.PLAYLISTS + "/" + playlistId, TokenManager.getToken());
    }
}
