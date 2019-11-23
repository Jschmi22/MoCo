package de.guenthner.trackingapp.FirebaseDB;

public class UserData
{
    public String username;
    public String email;
    public String timestamp;

    public UserData()
    {

    }

    public UserData(String username, String email, String timestamp)
    {
        this.username = username;
        this.email = email;
        this.timestamp = timestamp;
    }
}



