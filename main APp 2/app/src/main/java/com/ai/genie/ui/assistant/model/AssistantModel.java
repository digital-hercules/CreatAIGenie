package com.ai.genie.ui.assistant.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AssistantModel implements Serializable {
    public String id;
    public String assistant;
    public String cat_id;
    public String title;
    public String description;
    public String image;
    public String system;
    public String model;
    public ArrayList<SuggestionModel> suggestion = new ArrayList<>();
}
