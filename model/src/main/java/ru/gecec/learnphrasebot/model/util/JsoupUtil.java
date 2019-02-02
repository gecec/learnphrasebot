package ru.gecec.learnphrasebot.model.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public abstract class JsoupUtil {
    public JsoupUtil() {
    }

    public static String removeAllHtmlTagsV2(String text) {
        return text(Jsoup.parse(text));
    }

    private static String text(Element element) {
        final StringBuilder accum = new StringBuilder();
        (new NodeTraversor(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                    accum.append(textNode.getWholeText());
                } else if (node instanceof Element) {
                    Element element = (Element) node;
                    if (accum.length() > 0 && (element.isBlock() || element.tag().getName().equals("br")) && !JsoupUtil.lastCharIsWhitespace(accum)) {
                        accum.append(" ");
                    }
                }

            }

            public void tail(Node node, int depth) {
            }
        })).traverse(element);
        return accum.toString().trim();
    }

    private static boolean lastCharIsWhitespace(StringBuilder sb) {
        return sb.length() != 0 && sb.charAt(sb.length() - 1) == ' ';
    }
}
