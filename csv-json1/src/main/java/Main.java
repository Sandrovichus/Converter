import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2, "data2.json");

//        String json3 = readString("new_data.json");
//
//        List<Employee> list3 = jsonToList(json3);
//        System.out.println(list3);


    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String listToJson(List<Employee> list) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter file = new
                FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        long id = 0;
        String firstName = "";
        String lastName = "";
        String country = "";
        int age = 0;
        List<Employee> employeeList = new ArrayList<Employee>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fileName);
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element elem1 = (Element) node;
                NodeList nodeList1 = elem1.getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node1 = nodeList1.item(j);
                    if (Node.ELEMENT_NODE == node1.getNodeType()) {
                        Element elem2 = (Element) node1;
                        String result = elem2.getTextContent();
                        switch (elem2.getNodeName()) {
                            case ("id"):
                                id = Long.parseLong(result);
                                break;
                            case ("firstName"):
                                firstName = result;
                                break;
                            case ("lastName"):
                                lastName = result;
                                break;
                            case ("country"):
                                country = result;
                                break;
                            case ("age"):
                                age = Integer.parseInt(result);
                                break;
                            default:
                                break;
                        }
                    }
                }

                Employee employee = new Employee(id, firstName, lastName, country, age);
                employeeList.add(employee);
            }
        }
        return employeeList;
    }

//    public static String readString(String fileName) {
//        JSONParser parser = new JSONParser();
//        try {
//            Object obj = parser.parse(new FileReader(fileName));
//            JSONObject jsonObject = (JSONObject) obj;
//            return jsonObject.toString();
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//
//    public static List<Employee> jsonToList(String json) {
//        List<Employee> list = new ArrayList<Employee>();
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();
//        JSONParser parser = new JSONParser();
//        try {
//            Object obj = parser.parse(json);
//            JSONObject jsonObject = (JSONObject) obj;
//            JSONArray employees = (JSONArray) jsonObject.get("Employee");
//            for (Object object : employees) {
//                Employee employee = gson.fromJson(,Employee.class);
//                list.add(employee);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
}






