package nl.arkenbout.geoffrey.newsreader.service.responses;


import java.util.ArrayList;

import nl.arkenbout.geoffrey.newsreader.model.Article;

public class ArticleResult {
    private ArrayList<Article> Results;
    private int NextId;

    public ArrayList<Article> getResults() {
        return this.Results;
    }

    public void setResults(ArrayList<Article> Results) {
        this.Results = Results;
    }

    public int getNextId() {
        return this.NextId;
    }

    public void setNextId(int NextId) {
        this.NextId = NextId;
    }
}
