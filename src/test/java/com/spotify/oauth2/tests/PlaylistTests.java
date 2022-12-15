package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.utils.FakerUtils.generateName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Spotify Oauth 2.0")
@Feature("Playlist API")
public class PlaylistTests extends BaseTest{

    @Story("Create a playlist story")
    @Link("https://example.org")
    @Link(name = "allure", type = "mylink")
    @TmsLink(("12345"))
    @Issue("1234567")
    @Description("this is the description")
    @Test(description="should be able to create a playlist")
    public void ShouldBeAbleToCreatePlayList() {
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertPlaylistEqual(response.as(Playlist.class), requestPlaylist);
    }

    @Test
    public void ShouldBeAbleToGetAPlayList() {
        Playlist requestPlaylist = playlistBuilder("Updated playlist name", "Updated playlist description", false);
        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
        assertPlaylistEqual(response.as(Playlist.class), requestPlaylist);
    }

    @Test
    public void ShouldBeAbleToUpdateAPlayList() {
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(),  false);
        Response response = PlaylistApi.update(requestPlaylist, DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);


    }

    @Story("Create a playlist story")
    @Test
    public void ShouldNotBeAbleToCreatePlayListWithoutName() {
        Playlist requestPlaylist = playlistBuilder("", generateDescription(), false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400);
        assertError(response.as(Error.class), StatusCode.CODE_400);
    }

    @Story("Create a playlist story")
    @Test
    public void ShouldNotBeAbleToCreatePlayListWithExpiredToken() {
        String invalid_token = "12345";
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);
        Response response = PlaylistApi.post(invalid_token, requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_401);
        assertError(response.as(Error.class), StatusCode.CODE_401);
    }

    @Step()
    public Playlist playlistBuilder(String name, String description, boolean _public) {
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();

    }

    @Step
    public void assertPlaylistEqual(Playlist responsePlayList, Playlist requestPlayList) {
        assertThat(responsePlayList.getName(), equalTo(requestPlayList.getName()));
        assertThat(responsePlayList.getDescription(), equalTo(requestPlayList.getDescription()));
        assertThat(responsePlayList.get_public(), equalTo(requestPlayList.get_public()));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode) {
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }

    @Step
    public void assertError(Error responseError, StatusCode statusCode) {
        assertThat(responseError.getError().getStatus(), equalTo(statusCode.code));
        assertThat(responseError.getError().getMessage(), equalTo(statusCode.msg));
    }
}
