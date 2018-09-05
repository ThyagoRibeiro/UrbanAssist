package br.com.polieach.urbanassist.view;

/**
 * Created by Thyag on 03/02/2018.
 */

public class RowData {
    private String title;
    private String subtitle;

    public RowData() {
    }

    public RowData(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}