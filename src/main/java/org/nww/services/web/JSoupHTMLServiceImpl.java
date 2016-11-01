/**
 * 
 */
package org.nww.services.web;

import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

/**
 * An {@link HTMLService} implementation using the Jsoup framework.
 * @author mga
 *
 */
public class JSoupHTMLServiceImpl implements HTMLService {

	private static final String HTML_TAG_BR = "<br />";
	private static final String HTML_TAG_IMG = "img";
	private static final String HTML_ATTRIBUTE_SRC = "src";
	private static final String HTML_ATTRIBUTE_STYLE = "style";
	private static final String CSS_ATTRIBUTE_WIDTH = "width";
	private static final String CSS_ATTRIBUTE_HEIGHT = "height";
	private static final String[] NEWLINE_MARKERS = { "\\r\\n" };
	
	/* (non-Javadoc)
	 * @see org.nww.services.web.HTMLService#removeHtmlTags(java.lang.String)
	 */
	@Override
	public String removeHTMLTags(String html) {
		return Jsoup.parse(html).text();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.services.web.HTMLService#optimizedHTMLImageTags(java.lang.String)
	 */
	@Override
	public String optimizedHTMLImageTags(String html) {
		Document doc = Jsoup.parse(html);
		Elements imgElements = doc.getElementsByTag(HTML_TAG_IMG);
		imgElements.forEach(imgElement -> {
			if(imgElement.hasAttr(HTML_ATTRIBUTE_STYLE)) {
				writeCSSImageWidthToFileName(imgElement);
			}
		});
		
		return doc.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.nww.services.web.HTMLService#convertNewlinesToHTML(java.lang.String)
	 */
	@Override
	public String convertNewlinesToHTML(String withNewlines) {
		if(StringUtils.isEmpty(withNewlines)) {
			return withNewlines;
		}
		
		String converted = new String(withNewlines);
		for (int i = 0; i < NEWLINE_MARKERS.length; i++) {
			converted = converted.replaceAll(NEWLINE_MARKERS[i], HTML_TAG_BR);
		}
		return converted;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.services.web.HTMLService#convertHTMLBreaksToNewlines(java.lang.String)
	 */
	@Override
	public String convertHTMLBreaksToNewlines(String withBreaks) {
		if(StringUtils.isEmpty(withBreaks)) {
			return withBreaks;
		}
		
		String converted = withBreaks.replaceAll(HTML_TAG_BR, "\r\n");
		
		return converted;
	}
	
	/**
	 * Checks the source of the image element for file size information and tries to append them in case 
	 * they are missing.
	 * @param imgElement the HTML image element whose src attribute to be updated
	 */
	private void writeCSSImageWidthToFileName(Element imgElement) {
		String srcAttr = imgElement.attr(HTML_ATTRIBUTE_SRC);
		
		// check if there is anything to do at all
		if(isImageSizeAlreadyWithinURL(srcAttr)) {
			return;
		}
		
		// image width seems to be missing -> apply
		Optional<Integer> width = findWidthInformationFromCSSSTyles(imgElement.attr(HTML_ATTRIBUTE_STYLE));
		if(width.isPresent()) {
			srcAttr += "_" + width.get();
		}
		Optional<Integer> height = findHeightInformationFromCSSStyles(imgElement.attr(HTML_ATTRIBUTE_STYLE));
		if(height.isPresent()) {
			srcAttr += "_" + height.get();
		}
		
		imgElement.attr(HTML_ATTRIBUTE_SRC, srcAttr);
	}
	
	/**
	 * Checks the passed url string for an underscore within the last part of the url splitted by "/".
	 * @param url the url to be checked
	 * @return true if the last part contains an underscore
	 */
	private boolean isImageSizeAlreadyWithinURL(String url) {
		String[] parts = url.split("/");
		String possibleFileName = parts[parts.length - 1];
		
		return possibleFileName.contains("_");
	}
	
	/**
	 * Tries to find width information within CSS styles.
	 * @param styleAttr the HTML style attribute value
	 * @return the CSS defined width if found
	 */
	private Optional<Integer> findWidthInformationFromCSSSTyles(String styleAttr) {
		String possibleValue = findCSSStyleWithinAttributeValue(styleAttr, CSS_ATTRIBUTE_WIDTH);
		
		if(null != possibleValue) {
			return Optional.of(Integer.parseInt(possibleValue.replaceAll("px", "")));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Tries to find height information within CSS styles.
	 * @param styleAttr the HTML style attribute value
	 * @return the CSS defined heigth if found
	 */
	private Optional<Integer> findHeightInformationFromCSSStyles(String styleAttr) {
		String possibleValue = findCSSStyleWithinAttributeValue(styleAttr, CSS_ATTRIBUTE_HEIGHT);
		
		if(null != possibleValue) {
			return Optional.of(Integer.parseInt(possibleValue));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Find a named CSS attribute within a HTML style attribute value. 
	 * @param styleAttributeValue the HTML attribute value to search in
	 * @param styleAttribute the attribute name to lookup
	 * @return the value of the found CSS attribute or null if none is found
	 */
	private String findCSSStyleWithinAttributeValue(String styleAttributeValue, String styleAttribute) {
		String[] styles = styleAttributeValue.replaceAll(" +", "").split(";");
		
		for (String style : styles) {
			if(style.startsWith(styleAttribute)) {
				return style.split(":")[1];
			}
		}
		
		return null;
	}
}
