package com.shopme.admin.user.exporter;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter{
    public void export(List<User> users, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
        csvBeanWriter.writeHeader(csvHeader);

        users.forEach(user -> {
            try {
                csvBeanWriter.write(user, fieldMapping);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        csvBeanWriter.close();
    }
}
