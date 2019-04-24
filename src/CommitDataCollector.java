/**
 * This file is created by jubair
 * Date: 4/16/19 Time: 11:30 AM
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class CommitDataCollector {

    public void storeInXML(String xmlFilePath, List<Commit> commitList, String projectName) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFilePath);

        Element root = document.createElement(projectName);
        document.appendChild(root);

        for (Commit singleCommit :
                commitList) {
            Element commit = document.createElement("commit");

            Element hash = document.createElement("hash");
            Element message = document.createElement("message");
            Element author = document.createElement("author");

            message.appendChild(document.createTextNode(singleCommit.message));
            commit.appendChild(message);

            author.appendChild(document.createTextNode(singleCommit.author));
            commit.appendChild(author);

            hash.appendChild(document.createTextNode(singleCommit.hash));
            commit.appendChild(hash);

            root.appendChild(commit);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));

        transformer.transform(domSource, streamResult);

        System.out.println("Done creating XML File");
    }

    private List<Commit> extractCommitInfo(String gitRepository) throws IOException {
        List<Commit> commitList = new ArrayList<>();
        ExecCommand execCommand = new ExecCommand();
        String exec = execCommand.exec("git log --pretty=\"%H,%an,%s\"", gitRepository);
        String[] commits = exec.split("\n");
        for (String commit:
             commits) {
            String[] info = commit.split(",");
            if(info.length > 3) {
                String hash = info[0];
                String author = info[1];
                String message = info[2];
                Commit currentCommit = new Commit(hash, message, author);
                commitList.add(currentCommit);
            }
        }
        return commitList;
    }

    public static void main(String argv[]) {
        try {
            CommitDataCollector commitDataCollector = new CommitDataCollector();
            List<Commit> commitList = commitDataCollector.extractCommitInfo("/home/jubair/Documents/maven");
            commitDataCollector.storeInXML("/home/jubair/Documents/CommitCrawler_maven.xml", commitList, "maven");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


}
