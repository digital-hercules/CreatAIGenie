package com.ai.genie.inapprewardsysytem.inapprewardsysytem;
public class RewardStatus {
    public boolean available;
    public int streak;
    public Long remainingTime;

    public int points = 0;

    public RewardStatus() {}

    public RewardStatus(boolean available, int streak, Long remainingTime,int points) {
        this.available = available;
        this.streak = streak;
        this.remainingTime = remainingTime;
        this.points=points;
    }
}