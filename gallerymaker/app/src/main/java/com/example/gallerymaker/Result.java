package com.example.gallerymaker;

public class Result {
    private final int[] mNumber;
    private final String[] mallHashtags;
    private final String[] mHashtags;
    private final long mTimeCost;

    public Result(float[] probs, long timeCost) {
        mallHashtags = getAllHashtags();
        mNumber = argmax(probs);
        mHashtags  =getGoodHashtags(mallHashtags, mNumber);
        mTimeCost = timeCost;
    }
    public String[] getAllHashtags() {
        return new String[]{"fashion", "selfie" , "ulzzang" , "smile",  "selca", "music", "ootd", "food", "art", "me", "girl",  "nature", "cute","friends", "boy", "catstagram", "dogstagram", "caturday", "doge"};
    }

    public String[] getGoodHashtags(String[] mallHashtags, int[] mNumber) {
        String[] mHashtags = new String[] {mallHashtags[mNumber[0]], mallHashtags[mNumber[1]], mallHashtags[mNumber[2]]};
        return mHashtags;
    }

    public int[] getNumber() {
        return mNumber;
    }

    public String getHashtags() {
        return "#" + mHashtags[0].toString() + " #" + mHashtags[1].toString() + " #" + mHashtags[2].toString();
    }

    public long getTimeCost() {
        return mTimeCost;
    }

    private static int[] argmax(float[] probs) {
        int maxIdx1 = -1;
        float maxProb1 = 0.0f;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb1) {
                maxProb1 = probs[i];
                maxIdx1 = i;
            }
        }
        float[] probs2 = new float[probs.length -1];
        int k=0;
        for (int i = 0; i < probs.length; i++) {
            if (i == maxIdx1) {
                k=1;
            }
            else {
                probs2[i - k] = probs[i];
            }
        }
        int maxIdx2 = -1;
        float maxProb2 = 0.0f;
        for (int i = 0; i < probs2.length; i++) {
            if (probs2[i] > maxProb2) {
                maxProb2 = probs2[i];
                maxIdx2 = i;
            }
        }


        float[] probs3 = new float[probs2.length -1];
        k=0;
        for (int i = 0; i < probs2.length; i++) {
            if (i == maxIdx1) {
                k=1;
            }
            else {
                probs3[i - k] = probs2[i];
            }
        }
        int maxIdx3 = -1;
        float maxProb3 = 0.0f;
        for (int i = 0; i < probs3.length; i++) {
            if (probs3[i] > maxProb3) {
                maxProb3 = probs3[i];
                maxIdx3 = i;
            }
        }


        return new int[] {maxIdx1, maxIdx2, maxIdx3};
    }
}