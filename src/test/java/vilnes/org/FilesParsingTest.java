package vilnes.org;


import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import vilnes.org.modal.jsonModal;


import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {

    @Test
    void zipFileTest() throws Exception {
        ClassLoader cl = FilesParsingTest.class.getClassLoader();
        {
            try (InputStream resources = cl.getResourceAsStream("resources/dataFortest.zip");
                 ZipInputStream zis = new ZipInputStream(resources)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().contains("sample.pdf")) {
                        PDF textPDF = new PDF(zis);
                        assertThat(textPDF.text).contains("A Simple PDF File");
                    } else if (entry.getName().contains("file_example_XLS_50.xls")) {
                        XLS contentXLS = new XLS(zis);
                        assertThat(contentXLS.excel.getSheetAt(0).getRow(5).getCell(1).getStringCellValue()).contains("Nereida");
                    } else if (entry.getName().contains("addresses.csv")) {
                        CSVReader reader = new CSVReader(new InputStreamReader(zis));
                        List<String[]> contentCSV = reader.readAll();
                        assertThat(contentCSV.get(0)[0]).contains("John");
                    }
                }
            }
        }
    }

    @Test
    void jsonParsTest() throws Exception {
        ClassLoader cl = FilesParsingTest.class.getClassLoader();
        ObjectMapper objectMapper = new ObjectMapper();
        try (
                InputStream resource = cl.getResourceAsStream("resources/example.json")
        ) {
            jsonModal jsonModal = objectMapper.readValue(resource, jsonModal.class);
            assertThat(jsonModal.name).isEqualTo("Dmitry Rodichev");
            assertThat(jsonModal.id).isEqualTo("21231123");
            assertThat(jsonModal.age).isEqualTo(111);
            assertThat(jsonModal.married).isFalse();
            assertThat(jsonModal.address.street).isEqualTo("2170 Wakefield Street");
            assertThat(jsonModal.address.city).isEqualTo("Philadelphia");
            assertThat(jsonModal.address.country).isEqualTo("USA");
        }
    }
}