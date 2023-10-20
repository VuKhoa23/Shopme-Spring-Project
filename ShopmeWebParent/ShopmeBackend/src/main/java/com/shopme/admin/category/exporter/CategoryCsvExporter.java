package com.shopme.admin.category.exporter;

import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter{
    public void export(List<Category> categories, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Category ID", "Name", "Alias", "Enabled"};
        String[] fieldMapping = {"id", "name", "alias", "enabled"};
        csvBeanWriter.writeHeader(csvHeader);

        categories.forEach(category -> {
            try {
                csvBeanWriter.write(category, fieldMapping);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        csvBeanWriter.close();
    }


}
