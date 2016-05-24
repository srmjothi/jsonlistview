package com.example.jo265962.jsonlistview.nework;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JO265962 on 5/21/2016.
 */
public class JSONResponse {
    private String title;
    private List<JsonRow> rows = new ArrayList<JsonRow>();

    public String getTitle() {
        return title;
    }

    public List<JsonRow> jsonRows() {
        return rows;
    }


}
