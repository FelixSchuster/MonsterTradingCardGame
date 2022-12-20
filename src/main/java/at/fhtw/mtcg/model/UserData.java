package at.fhtw.mtcg.model;

import com.fasterxml.jackson.annotation.JsonAlias;
public class UserData {
    @JsonAlias({"Name"})
    String name;
    @JsonAlias({"Bio"})
    String bio;
    @JsonAlias({"Image"})
    String image;

    // Jackson needs the default constructor
    public UserData() {}
    public UserData(String name, String bio, String image) {
        this.name = name;
        this.bio = bio;
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
