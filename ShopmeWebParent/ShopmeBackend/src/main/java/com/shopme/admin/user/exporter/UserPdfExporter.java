package com.shopme.admin.user.exporter;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter{

    public void export(List<User> users, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "application/pdf", ".pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(13);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("List of users", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);
        table.setSpacingBefore(10);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 3.0f, 1.5f});

        writeTableHeader(table);
        writeTableData(users, table);
        document.add(table);


        document.close();
    }

    private void writeTableData(List<User> users, PdfPTable table) {
        for (User user : users) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.isEnabled()));
        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(13);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("User ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enabled", font));
        table.addCell(cell);

    }
}
