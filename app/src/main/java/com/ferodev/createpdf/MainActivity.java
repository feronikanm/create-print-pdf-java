package com.ferodev.createpdf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btn_create_pdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_create_pdf = (Button)findViewById(R.id.btn_create_pdf);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        btn_create_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createPDFFile(Comman.getAppPath(MainActivity.this)+"test_pdf.pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();

    }

    private void createPDFFile(String path) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        if(new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();
            //Save
            PdfWriter.getInstance(document,new FileOutputStream(path));
            //open to write
            document.open();
            //Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("author");
            document.addCreator("author");

            //Font Settings
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSizeHeader = 20.0f;
            float fontSizeBody = 16.0f;
            float valueFontSize = 26.0f;

            //Custom font
            BaseFont fontName = BaseFont.createFont("res/font/roboto.ttf","UTF-8",BaseFont.EMBEDDED);

            //create title of document
            Font titleFont = new Font(fontName,20.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Order Deatils", Element.ALIGN_CENTER,titleFont);

            // Add more
            Font orderNumberFont = new Font(fontName,fontSizeHeader,Font.NORMAL,colorAccent);
            addNewItem(document,"order number",Element.ALIGN_LEFT,orderNumberFont);

            Font orderNumberValueFont = new Font(fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"#525263",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document,"Order Date",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,date,Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document,"Account name",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,"Fero",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            //Add product order detail
            addLineSpace(document);
            addNewItem(document,"Product details",Element.ALIGN_CENTER,titleFont);

            addLineSeperator(document);

            //item 1
            addNewItemWithLeftAndRight(document,"Burger","(1.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"20","1200.0",titleFont,orderNumberValueFont);

            addLineSeperator(document);

            //item 2
            addNewItemWithLeftAndRight(document,"Pizza","(0.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"12","1520.0",titleFont,orderNumberValueFont);

            addLineSeperator(document);

            //item 3
            addNewItemWithLeftAndRight(document,"Sandwich","(0.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"10","1000.0",titleFont,orderNumberValueFont);

            addLineSeperator(document);

            //Total
            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document,"total","8500",titleFont,orderNumberValueFont);

            document.close();
            Toast.makeText(this,"Sucess",Toast.LENGTH_SHORT).show();

            printPDF();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);

    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont,Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft,textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight,textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);


    }

    private void addLineSeperator(Document document)throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));

    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void printPDF(){
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(MainActivity.this,Comman.getAppPath(MainActivity.this)+"test_pdf.pdf");
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
        }catch (Exception ex){
            Log.e("author",""+ex.getMessage());
            Toast.makeText(MainActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();

        }
    }

}