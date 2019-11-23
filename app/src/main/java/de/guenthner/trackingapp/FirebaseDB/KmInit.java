package de.guenthner.trackingapp.FirebaseDB;

public class KmInit
{
    public int kmFeet;
    public int kmBike;
    public int kmCar;
    public int kmOepnv;

    public KmInit()
    {

    }

    public KmInit(int kmFeet, int kmBike, int kmCar, int kmOepnv)
    {
        this.kmFeet = kmFeet;
        this.kmBike = kmBike;
        this.kmCar = kmCar;
        this.kmOepnv = kmOepnv;
    }
}
