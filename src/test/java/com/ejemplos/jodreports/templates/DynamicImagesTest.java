package com.ejemplos.jodreports.templates;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import com.ejemplos.jodreports.templates.image.ClasspathImageSource;
import com.ejemplos.jodreports.templates.image.FileImageSource;
import com.ejemplos.jodreports.templates.image.ImageSource;
import com.ejemplos.jodreports.templates.image.RenderedImageSource;

public class DynamicImagesTest extends AbstractTemplateTest {

	public void testOrderForm() throws Exception {
		File templateFile = getTestFile("order-with-images-template.odt");
		
		ImageSource red = new RenderedImageSource(ImageIO.read(new File("src/test/resources/red.png")));
		ImageSource blue = new FileImageSource("src/test/resources/blue.png");
		ImageSource blue2 = new ClasspathImageSource("blue.png");
		
		Map model = new HashMap();
		List items = new ArrayList();
		Map item1 = new HashMap();
		item1.put("description", "First Item");
		item1.put("quantity", "20");
		item1.put("picture", red);
		items.add(item1);
		Map item2 = new HashMap();
		item2.put("description", "Second Item");
		item2.put("quantity", "15");
		item2.put("picture", blue);
		items.add(item2);
		Map item3 = new HashMap();
		item3.put("description", "Third Item");
		item3.put("quantity", "50");
		item3.put("picture", red);
		items.add(item3);
		Map item4 = new HashMap();
		item4.put("description", "Fourth Item");
		item4.put("quantity", "20");
		item4.put("picture", blue2);
		items.add(item4);
		model.put("items", items);
		
		String content = processTemplate(templateFile, model);
		
		String expected =
			"Order Form\n" +
			"\n" +
			"Picture\n" +
			"Description\n" + 
			"Quantity\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-1.png]\n" +
			"First Item\n" +
			"20\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-2.png]\n" +
			"Second Item\n" +
			"15\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-1.png]\n" +
			"Third Item\n" +
			"50\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-3.png]\n" +
			"Fourth Item\n" +
			"20";			
		assertEquals("incorrect output", expected, content);		
	}

	public void testNewScriptOrderForm() throws Exception {
		File templateFile = getTestFile("order-with-images-template-2.odt");
		
		ImageSource red = new RenderedImageSource(ImageIO.read(new File("src/test/resources/red.png")));
		ImageSource blue = new FileImageSource("src/test/resources/blue.png");
		ImageSource blue2 = new ClasspathImageSource("blue.png");
		
		Map model = new HashMap();
		List items = new ArrayList();
		Map item1 = new HashMap();
		item1.put("description", "First Item");
		item1.put("quantity", "20");
		item1.put("picture", red);
		items.add(item1);
		Map item2 = new HashMap();
		item2.put("description", "Second Item");
		item2.put("quantity", "15");
		item2.put("picture", blue);
		items.add(item2);
		Map item3 = new HashMap();
		item3.put("description", "Third Item");
		item3.put("quantity", "50");
		item3.put("picture", red);
		items.add(item3);
		Map item4 = new HashMap();
		item4.put("description", "Fourth Item");
		item4.put("quantity", "20");
		item4.put("picture", blue2);
		items.add(item4);
		model.put("items", items);
		
		String content = processTemplate(templateFile, model);
		
		String expected =
			"Order Form\n" +
			"\n" +
			"Picture\n" +
			"Description\n" + 
			"Quantity\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-1.png]\n" +
			"First Item\n" +
			"20\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-2.png]\n" +
			"Second Item\n" +
			"15\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-1.png]\n" +
			"Third Item\n" +
			"50\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-3.png]\n" +
			"Fourth Item\n" +
			"20";			
		assertEquals("incorrect output", expected, content);		
	}
	
	public void testImageSourceWithFileName() throws Exception {
		File templateFile = getTestFile("order-with-images-template.odt");
		
		ImageSource blue = new FileImageSource("src/test/resources/blue.png");

		Map model = new HashMap();
		List items = new ArrayList();
		Map item1 = new HashMap();
		item1.put("description", "First Item");
		item1.put("quantity", "20");
		item1.put("picture", blue);
		items.add(item1);
		Map item2 = new HashMap();
		item2.put("description", "Second Item");
		item2.put("quantity", "15");
		item2.put("picture", "src/test/resources/red.png");
		items.add(item2);
		Map item3 = new HashMap();
		item3.put("description", "Third Item");
		item3.put("quantity", "50");
		item3.put("picture", "");
		items.add(item3);
		Map item4 = new HashMap();
		item4.put("description", "Fourth Item");
		item4.put("quantity", "20");
		item4.put("picture", null);
		items.add(item4);
		model.put("items", items);
		
		String content = processTemplate(templateFile, model);
		
		String expected =
			"Order Form\n" +
			"\n" +
			"Picture\n" +
			"Description\n" + 
			"Quantity\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-1.png]\n" +
			"First Item\n" +
			"20\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/dynamic-image-2.png]\n" +
			"Second Item\n" +
			"15\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/1000000000000028000000145B20E0B1.png]\n" +
			"Third Item\n" +
			"50\n" +
			"[frame:1.409cm,0.706cm][img:Pictures/1000000000000028000000145B20E0B1.png]\n" +
			"Fourth Item\n" +
			"20";			
		assertEquals("incorrect output", expected, content);		
	}

	public void testOrderFormWithImageResize() throws Exception {
		File templateFile = getTestFile("order-with-images-resize-template.odt");
		
		ImageSource red = new RenderedImageSource(ImageIO.read(new File("src/test/resources/red.png")));
		ImageSource blue = new FileImageSource("src/test/resources/blue.png");
		ImageSource blue2 = new ClasspathImageSource("blue.png");

		Map model = new HashMap();
		List items = new ArrayList();
		Map item1 = new HashMap();
		item1.put("description", "First Item");
		item1.put("quantity", "20");
		item1.put("picture", red);
		items.add(item1);
		Map item2 = new HashMap();
		item2.put("description", "Second Item");
		item2.put("quantity", "15");
		item2.put("picture", blue);
		items.add(item2);
		Map item3 = new HashMap();
		item3.put("description", "Third Item");
		item3.put("quantity", "50");
		item3.put("picture", red);
		items.add(item3);
		Map item4 = new HashMap();
		item4.put("description", "Fourth Item");
		item4.put("quantity", "20");
		item4.put("picture", blue2);
		items.add(item4);
		model.put("items", items);
		
		String content = processTemplate(templateFile, model);
		
		String expected =
			"Order Form\n" +
			"\n" +
			"Picture\n" +
			"Description\n" + 
			"Quantity\n" +
			"[frame:8.999cm,4.5cm][img:Pictures/dynamic-image-1.png]\n" +
			"First Item\n" +
			"20\n" +
			"[frame:8.999cm,4.5cm][img:Pictures/dynamic-image-2.png]\n" +
			"Second Item\n" +
			"15\n" +
			"[frame:8.999cm,4.5cm][img:Pictures/dynamic-image-1.png]\n" +
			"Third Item\n" +
			"50\n" +
			"[frame:8.999cm,4.5cm][img:Pictures/dynamic-image-3.png]\n" +
			"Fourth Item\n" +
			"20";			
		assertEquals("incorrect output", expected, content);		
	}
	
	public void testImageResize() throws Exception {
		File templateFile = getTestFile("images-resize-template.odt");
        Map model = new HashMap();
        model.put("coinsV", new FileImageSource("src/test/resources/coinsV.jpg"));
        model.put("coinsH", new FileImageSource("src/test/resources/coinsH.jpg"));
        model.put("earthV", new ClasspathImageSource("earthV.jpg"));
        model.put("earthH", new ClasspathImageSource("earthH.jpg"));
        String actual = processTemplate(templateFile, model);
		String expected =
			"Default\n" +
			"[frame:2.54cm,2.54cm][img:Pictures/dynamic-image-1.png]\n" +
			"[frame:2.54cm,2.54cm][img:Pictures/dynamic-image-2.png]\n" +
			"[frame:2.54cm,2.54cm][img:Pictures/dynamic-image-3.png]\n" +
			"[frame:2.54cm,2.54cm][img:Pictures/dynamic-image-4.png]\n" +
			"MaxWidth\n" +
			"[frame:2.54cm,1.57cm][img:Pictures/dynamic-image-1.png]\n" +
			"[frame:2.54cm,4.12cm][img:Pictures/dynamic-image-2.png]\n" +
			"[frame:2.54cm,1.9cm][img:Pictures/dynamic-image-3.png]\n" +
			"[frame:2.54cm,3.39cm][img:Pictures/dynamic-image-4.png]\n" +
			"MaxHeight\n" +
			"[frame:4.12cm,2.54cm][img:Pictures/dynamic-image-1.png]\n" +
			"[frame:1.57cm,2.54cm][img:Pictures/dynamic-image-2.png]\n" +
			"[frame:3.39cm,2.54cm][img:Pictures/dynamic-image-3.png]\n" +
			"[frame:1.9cm,2.54cm][img:Pictures/dynamic-image-4.png]\n" +
			"Fit\n" +
			"[frame:2.54cm,1.57cm][img:Pictures/dynamic-image-1.png]\n" +
			"[frame:1.57cm,2.54cm][img:Pictures/dynamic-image-2.png]\n" +
			"[frame:2.54cm,1.9cm][img:Pictures/dynamic-image-3.png]\n" +
			"[frame:1.9cm,2.54cm][img:Pictures/dynamic-image-4.png]";			
        
        assertEquals("output content", expected, actual);
	}

	public void testImageResizeInLocaleWithCommaAsDecimalSeparator()
			throws Exception {
		Locale locale = Locale.getDefault();
		try {
			Locale.setDefault(Locale.US);
			testImageResize();
		} finally {
			Locale.setDefault(locale);
		}
	}
}
