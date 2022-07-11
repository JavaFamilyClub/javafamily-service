package club.javafamily.view;

import com.deepoove.poi.data.BookmarkTextRenderData;
import com.deepoove.poi.plugin.highlight.HighlightRenderData;

import java.util.List;

public class Definition {
    private BookmarkTextRenderData name;
    List<Property> properties;
    HighlightRenderData definitionCode;

    public BookmarkTextRenderData getName() {
        return name;
    }

    public void setName(BookmarkTextRenderData name) {
        this.name = name;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public HighlightRenderData getDefinitionCode() {
        return definitionCode;
    }

    public void setDefinitionCode(HighlightRenderData definitionCode) {
        this.definitionCode = definitionCode;
    }

}