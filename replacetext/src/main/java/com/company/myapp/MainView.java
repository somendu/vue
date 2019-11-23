package com.company.myapp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.company.myapp.backend.service.UploadService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;

@Route
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class MainView extends VerticalLayout {

	ByteArrayOutputStream attachmentData;
	String attachmentFileName;

	private File file;

	public MainView() {

		Div output = new Div();

//		Receiver receiver = new Receiver() {
//
//			@Override
//			public OutputStream receiveUpload(String filename, String mimeType) {
//
//				attachmentData = new ByteArrayOutputStream();
//				attachmentFileName = filename;
//				return attachmentData;
//			}
//		};
//
//		Upload upload = new Upload(receiver);

//		MemoryBuffer buffer = new MemoryBuffer();
//		Upload upload = new Upload(buffer);
//
//		upload.addSucceededListener(event -> {
//			Component component = createComponent(event.getMIMEType(), event.getFileName(), buffer.getInputStream());
//			// showOutput(event.getFileName(), component, output);
//		});

		FileBuffer fileBuffer = new FileBuffer();
		Upload upload = new Upload(fileBuffer);
		upload.addFinishedListener(e -> {
			InputStream inputStream = fileBuffer.getInputStream();
			// read the contents of the buffered file
			// from inputStream

			try {
				String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			file = new File(fileBuffer.getFileName());

			try {
				FileUtils.copyInputStreamToFile(inputStream, file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		// TODO Check here for text or xml file only

		TextField searchField = new TextField();
		searchField.setLabel("Search Text");

		// TODO Get the values
		searchField.getValue();

		TextField replaceField = new TextField();
		replaceField.setLabel("Replace Text");

		// TODO Get the values
		replaceField.getValue();

		Button rndButton = new Button();
		rndButton.setText("Replace and Download");

		// TODO Service for Taking the text file or xml file

		// TODO Call service to read the file find text replace

		add(upload, output, searchField, replaceField, rndButton);

		rndButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {

				// TODO Validate for empty strings or file etc

				if (attachmentData == null) {
					System.out.println("attachmentData is null");
				}

				System.out.println(
						"Returned here : " + UploadService.getInstance().readFile(file, fileBuffer.getFileName()));

				// TODO Get the file downloaded

			}
		});

	}

	private Component createComponent(String mimeType, String fileName, InputStream stream) {
		if (mimeType.startsWith("text")) {
			return createTextComponent(stream);
		} else if (mimeType.startsWith("image")) {
			Image image = new Image();
			try {

				byte[] bytes = IOUtils.toByteArray(stream);
				image.getElement().setAttribute("src",
						new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));
				try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
					final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
					if (readers.hasNext()) {
						ImageReader reader = readers.next();
						try {
							reader.setInput(in);
							image.setWidth(reader.getWidth(0) + "px");
							image.setHeight(reader.getHeight(0) + "px");
						} finally {
							reader.dispose();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return image;
		}
		Div content = new Div();
		String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'", mimeType,
				MessageDigestUtil.sha256(stream.toString()));
		content.setText(text);
		return content;

	}

	private Component createTextComponent(InputStream stream) {
		String text;
		try {
			text = IOUtils.toString(stream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			text = "exception reading stream";
		}
		return new Text(text);
	}

	private void showOutput(String text, Component content, HasComponents outputContainer) {
		HtmlComponent p = new HtmlComponent(Tag.P);
		p.getElement().setText(text);
		// outputContainer.add(p);
		// outputContainer.add(content);
	}

}
