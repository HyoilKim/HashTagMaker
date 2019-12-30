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
        return new String[]{"fashion", "ootd", "daily", "food", "yummy", "foodstagram", "me", "selfie", "ulzzang", "catstagram", "caturday", "catoftheday", "dogstagram", "doge", "puppylove", "instagood", "picoftheday", "f4f"};
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

        int maxIdx2 = -1;
        float maxProb2 = 0.0f;
        for (int i = 0; i < probs.length; i++) {
            if(i == maxIdx1){
                continue;
            }
            else if (probs[i] > maxProb2) {
                maxProb2 = probs[i];
                maxIdx2 = i;
            }
        }


        int maxIdx3 = -1;
        float maxProb3 = 0.0f;
        for (int i = 0; i < probs.length; i++) {
            if(i == maxIdx1 || i == maxIdx2){
                continue;
            }
            else if (probs[i] > maxProb3) {
                maxProb3 = probs[i];
                maxIdx3 = i;
            }
        }


        return new int[] {maxIdx1, maxIdx2, maxIdx3};
    }
}