package com.example.ojtmonitoring;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.print.PdfPrint;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class PrintReportActivity extends AppCompatActivity {
    private TextView contentHtmlText;
    private WebView webViewPage;
    private LinearLayout lnearlyout;
    private Button updateBtn;
    Bitmap bitmap;
    int width = 0;
    int height = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_report);

        lnearlyout = (LinearLayout)findViewById(R.id.lnearlyout);
        webViewPage = (WebView)findViewById(R.id.webViewPage);
        updateBtn = (Button)findViewById(R.id.updateBtn);


        StringBuffer htmlContent = new StringBuffer("<html>");
        htmlContent.append("<table border='1'>");
        htmlContent.append("<tr><th>Name</th><th>Address</th></tr> ");
        htmlContent.append("<tr><td>TEST HTML</td><td>TEST HTML1</td></tr> ");
        htmlContent.append("</table>");
        htmlContent.append("</html>");

        webViewPage.loadData(htmlContent.toString(),"text/html","utf-8");

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWebPrintJob(webViewPage);


             /*   width= 3000;
                height=3000;

                bitmap=  loadBitmap(lnearlyout,width,height);

                createPdf();*/
            }
        });

        updateBtn.callOnClick();
        /*

        StringBuffer htmlContent = new StringBuffer("<html>");
        htmlContent.append("<table border='1'>");
        htmlContent.append("<tr><th>Name</th><th>Address</th></tr> ");
        htmlContent.append("<tr><td>TEST HTML</td><td>TEST HTML1</td></tr> ");
        htmlContent.append("</table>");
        htmlContent.append("</html>");

        webViewPage.loadData(htmlContent.toString(),"text/html","utf-8");

        permission();

        updateBtn.callOnClick();*/

    }

   /* private void createWebPrintJob(WebView webView) {
        String jobName = getString(R.string.app_name) + " Document";
        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
        PdfPrint pdfPrint = new PdfPrint(attributes);
        pdfPrint.print(webView.createPrintDocumentAdapter(jobName), path, "output_" + System.currentTimeMillis() + ".pdf");
    }*/

    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());


        // Save the job object for later status checking
       // mPrintJob = printJob;
    }


    private void permission(){
        if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED)){

            if(ActivityCompat.shouldShowRequestPermissionRationale(PrintReportActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(PrintReportActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            }
        }
    }

    private void createPdf(){
        WindowManager wm =(WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;

        int converHeigt =(int)height, convertWidth = (int)width;

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth,converHeigt,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);
        convertWidth = 3000;
        converHeigt=3000;
        bitmap= Bitmap.createScaledBitmap(bitmap,convertWidth,converHeigt,true);

        paint.setColor(Color.WHITE);
        canvas.drawBitmap(bitmap,0,0,null);
        pdfDocument.finishPage(page);


        String targetPdf = Environment.getExternalStorageState();
                //"/sdcard/test.pdf";
        File filePath = new File(targetPdf);
        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(PrintReportActivity.this,"Error"+e,Toast.LENGTH_SHORT);
        }
        pdfDocument.close();

    }


    public static Bitmap loadBitmap(View v , int w , int h){
        Bitmap b = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
}
