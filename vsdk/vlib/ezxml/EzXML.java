package vsdk.vlib.ezxml;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerException;

import javax.xml.transform.TransformerConfigurationException;

import javax.xml.transform.OutputKeys;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.w3c.dom.Comment;

import java.io.File;

import java.io.IOException;

/**
 * XML Simplified.
 */
public class EzXML {
    private DocumentBuilderFactory factory;
    private DocumentBuilder parser = null;

    private Document document;

    private Element root;

    private XMLWriter_ writer;

    public static final String NEW_DOCUMENT = "NEW_DOCUMENT";

    public static final int OK = 0;
    public static final int PARSER_CONF_EXC = 1;
    public static final int SAX_EXC = 2;
    public static final int IO_EXC = 3;
    public static final int IS_NULL = 4;

    public static final int WRITER_TRANSFORMER_EXC = 5;
    public static final int WRITER_TRANSFORMER_CONF_EXC = 6;

    /**
     * Create new factory instance & create new document builder / etc..
     */
    public int new_() {
        factory = DocumentBuilderFactory.newInstance();

        try {
            parser = factory.newDocumentBuilder();
        } catch(ParserConfigurationException parserConfExc) {
            return PARSER_CONF_EXC;
        }

        writer = new XMLWriter_();

        writer.new_();

        return OK;
    }

    /**
     * Parse document. Use EzXML.NEW_DOCUMENT to create new document.
     *
     * @param file Path to XML.
     */
    public int parseDoc(String file) {
        if(parser == null) {
            return IS_NULL;
        }

        try {
            if(file.equals(NEW_DOCUMENT)) {
                document = parser.newDocument();
            } else {
                document = parser.parse(file);

                root = document.getDocumentElement();

                root.normalize();
            }

            writer.setDoc(document);
        } catch(SAXException saxExc) {
            return SAX_EXC;
        } catch(IOException ioExc) {
            return IO_EXC;
        }

        return OK;
    }

    /**
     * Get document root elements by tag name.
     *
     * @param tag Tag name.
     */
    public NodeList rootGetElemsByTag(String tag) {
        if(root == null) {
            return null;
        }

        return root.getElementsByTagName(tag);
    }

    /**
     * Get document root elements array-tree by tag name.
     *
     * @param tag Tag name.
     */
    public Element[] rootArrayTreeByTag(String tag) {
        return nodeListToElemArray(rootGetElemsByTag(tag));
    }

    /**
     * Get document root element by tag name.
     *
     * @param tag Tag name.
     * @param index Element index.
     */
    public Element rootTreeElementByTag(String tag, int index) {
        return rootArrayTreeByTag(tag)[index];
    }

    /**
     * Get document builder factory.
     */
    public DocumentBuilderFactory getFactory() {
        return factory;
    }

    /**
     * Get document builder.
     */
    public DocumentBuilder getDocumentBuilder() {
        return parser;
    }

    /**
     * Get document.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Get document root.
     */
    public Element getRoot() {
        return root;
    }

    /**
     * Get XML writer class.
     */
    public XMLWriter_ getWriter() {
        return writer;
    }

    /**
     * Convert NodeList into Node array.
     *
     * @param nodeList Node list.
     */
    public static Node[] nodeListToArray(NodeList nodeList) {
        Node[] nodeArray = new Node[nodeList.getLength()];

        for(int nodeIndex=0; nodeIndex < nodeList.getLength(); nodeIndex++) {
            nodeArray[nodeIndex] = nodeList.item(nodeIndex);
        }

        return nodeArray;
    }

    /**
     * Convert NodeList into Element array.
     *
     * @param nodeList Node list.
     */
    public static Element[] nodeListToElemArray(NodeList nodeList) {
        Element[] elemArray = new Element[nodeList.getLength()];

        for(int nodeIndex=0; nodeIndex < nodeList.getLength(); nodeIndex++) {
            Node node = nodeList.item(nodeIndex);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                elemArray[nodeIndex] = (Element) node;
            }
        }

        return elemArray;
    }

    /**
     * XML Writer.
     */
    public static class XMLWriter_ {
        private TransformerFactory tFactory;
        private Transformer transformer = null;

        private Document document;

        private Comment docComment;

        protected int new_() {
            tFactory = TransformerFactory.newInstance();

            try {
                transformer = tFactory.newTransformer();
            } catch(TransformerConfigurationException transformerConfExc) {
                return WRITER_TRANSFORMER_CONF_EXC;
            }

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            return OK;
        }

        protected void setDoc(Document doc) {
            document = doc;
        }

        /**
         * Create new element. Returns null if document is null.
         *
         * @param name Tag name.
         */
        public Element create(String name) {
            if(document == null) {
                return null;
            }

            return document.createElement(name);
        }

        /**
         * Create new element and append it to parent. Returns null if document is null.
         *
         * @param name Tag name.
         * @param parent Element parent.
         */
        public Element append(String name, Element parent) {
            if(document == null) {
                return null;
            }

            Element element = document.createElement(name);

            parent.appendChild(element);

            return element;
        }

        /**
         * Create new element and append it to parent. Returns null if document is null.
         *
         * @param name Tag name.
         * @param parent Element parent.
         */
        public Element append(String name, Document parent) {
            if(document == null) {
                return null;
            }

            Element element = document.createElement(name);

            parent.appendChild(element);

            return element;
        }

        /**
         * Create new element and append it to document root. Returns null if document is null.
         *
         * @param name Tag name.
         */
        public Element append(String name) {
            if(document == null) {
                return null;
            }

            Element element = document.createElement(name);

            document.getDocumentElement().appendChild(element);

            return element;
        }

        /**
         * Append element to parent.
         *
         * @param element Element.
         * @param parent Element parent.
         */
        public void appendElement(Element element, Element parent) {
            parent.appendChild(element);
        }

        /**
         * Append element to parent.
         *
         * @param element Element.
         * @param parent Element parent.
         */
        public void appendElement(Element element, Document parent) {
            parent.appendChild(element);
        }

        /**
         * Add comment before root element in document.
         *
         * @param comment Comment content.
         * @param nlBef Add newline before comment?
         * @param nlAf Add newline after comment?
         */
        public int addDocComment(String comment, boolean nlBef, boolean nlAf) {
            if(document == null) {
                return IS_NULL;
            }

            docComment = document.createComment((nlBef ? "\n" : "") + comment + (nlAf ? "\n" : ""));

            return OK;
        }

        /**
         * Add comment before root element in document.
         *
         * @param comment Comment content.
         */
        public int addDocComment(String comment) {
            return addDocComment(comment, true, true);
        }

        /**
         * Save XML file and write to file.
         *
         * @param file File name.
         */
        public int write(String file) {
            if(transformer == null || document == null) {
                return IS_NULL;
            }

            if(docComment != null) {
                document.insertBefore(docComment, document.getDocumentElement());
            }

            DOMSource domSrc = new DOMSource(document);

            StreamResult streamRes = new StreamResult(new File(file));

            try {
                transformer.transform(domSrc, streamRes);
            } catch (TransformerException transformerExc) {
                return WRITER_TRANSFORMER_EXC;
            }

            return OK;
        }

        /**
         * Get transformer factory.
         */
        public TransformerFactory getFactory() {
            return tFactory;
        }

        /**
         * Get transformer.
         */
        public Transformer getTransformer() {
            return transformer;
        }
    }
}
