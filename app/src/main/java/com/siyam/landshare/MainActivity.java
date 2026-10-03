package com.siyam.landshare;

import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends android.app.Activity {
    LinearLayout root, shareGrid, resultBox;
    EditText totalInput;
    TextView ana, gonda, kora, kranti, til;
    TextView resultShot, resultPct, resultKatha, resultBigha, resultAcre;

    final String[] anaSymbols = {"৴","৵","৶","৷","৷৴","৷৵","৷৶","৷৷","৷৷৴","৷৷৵","৷৷৶","৸","৸৴","৸৵","৸৶","১"};
    final String[] anaNames = {"১ আনা","২ আনা","৩ আনা","৪ আনা","৫ আনা","৬ আনা","৭ আনা","৮ আনা","৯ আনা","১০ আনা","১১ আনা","১২ আনা","১৩ আনা","১৪ আনা","১৫ আনা","১৬ আনা"};

    @Override public void onCreate(Bundle saved) {
        super.onCreate(saved);
        getWindow().setStatusBarColor(Color.rgb(247,248,250));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        buildUi();
    }

    int dp(float v){ return (int)(v*getResources().getDisplayMetrics().density + .5f); }
    TextView label(String s){ TextView t=new TextView(this); t.setText(s); t.setTextColor(Color.rgb(107,114,128)); t.setTextSize(13); return t; }
    GradientDrawableLike bg(int color, int stroke){ return new GradientDrawableLike(color, stroke, dp(14)); }

    void buildUi(){
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(18),dp(14),dp(18),dp(28)); root.setBackgroundColor(Color.rgb(247,248,250)); scroll.addView(root); setContentView(scroll);

        TextView title=new TextView(this); title.setText("জমির হিসাব"); title.setTextSize(25); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD); title.setTextColor(Color.rgb(17,24,39)); root.addView(title,new LinearLayout.LayoutParams(-1,dp(42)));

        LinearLayout totalCard=card(); root.addView(totalCard); totalCard.addView(label("মোট জমি"));
        LinearLayout totalRow=new LinearLayout(this); totalRow.setGravity(Gravity.CENTER_VERTICAL); totalRow.setPadding(0,dp(10),0,0); totalCard.addView(totalRow);
        totalInput=new EditText(this); totalInput.setHint("0"); totalInput.setTextSize(20); totalInput.setSingleLine(true); totalInput.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL); totalInput.setPadding(dp(13),0,dp(13),0); totalInput.setBackground(bg(Color.WHITE,Color.rgb(229,231,235))); totalRow.addView(totalInput,new LinearLayout.LayoutParams(0,dp(54),1));
        TextView unit=label("শতাংশ"); unit.setTextSize(15); unit.setTextColor(Color.rgb(17,24,39)); unit.setPadding(dp(12),0,0,0); totalRow.addView(unit,new LinearLayout.LayoutParams(dp(70),dp(54)));

        LinearLayout shareCard=card(); LinearLayout.LayoutParams scp=new LinearLayout.LayoutParams(-1,-2); scp.topMargin=dp(14); root.addView(shareCard,scp);
        shareGrid=new LinearLayout(this); shareGrid.setOrientation(LinearLayout.VERTICAL); shareCard.addView(shareGrid);
        addShareRow("আনা","৸৴",0); addShareRow("গণ্ডা","০",1); addShareRow("কড়া","০",2); addShareRow("ক্রান্তি","০",3); addShareRow("তিল","০",4);
        Button calc=new Button(this); calc.setText("ফলাফল"); calc.setTextSize(15); calc.setTypeface(Typeface.DEFAULT,Typeface.BOLD); calc.setAllCaps(false); calc.setTextColor(Color.WHITE); calc.setBackground(bg(Color.rgb(23,105,255),Color.rgb(23,105,255))); LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,dp(52)); cp.topMargin=dp(14); shareCard.addView(calc,cp); calc.setOnClickListener(v->calculate());

        resultBox=card(); resultBox.setVisibility(View.GONE); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.topMargin=dp(14); root.addView(resultBox,rp);
        TextView rt=new TextView(this); rt.setText("ফলাফল"); rt.setTextSize(16); rt.setTypeface(Typeface.DEFAULT,Typeface.BOLD); rt.setTextColor(Color.rgb(17,24,39)); resultBox.addView(rt);
        resultShot=bigResult(); resultBox.addView(resultShot,topMargin(dp(18)));
        resultPct=percentResult(); resultBox.addView(resultPct,topMargin(dp(3)));
        resultBox.addView(divider(),topMargin(dp(16)));
        resultKatha=resultLine("কাঠা"); resultBigha=resultLine("বিঘা"); resultAcre=resultLine("একর");
    }

    LinearLayout card(){ LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setPadding(dp(16),dp(15),dp(16),dp(15)); c.setBackground(bg(Color.WHITE,Color.rgb(229,231,235))); return c; }
    LinearLayout.LayoutParams topMargin(int m){ LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.topMargin=m; return p; }
    TextView bigResult(){ TextView t=new TextView(this); t.setTextSize(25); t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); t.setTextColor(Color.rgb(17,24,39)); return t; }
    TextView percentResult(){ TextView t=new TextView(this); t.setTextSize(16); t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); t.setTextColor(Color.rgb(23,105,255)); return t; }
    View divider(){ View v=new View(this); v.setBackgroundColor(Color.rgb(229,231,235)); return v; }
    TextView resultLine(String name){ TextView t=new TextView(this); t.setText(name+" — "); t.setTextSize(15); t.setTextColor(Color.rgb(55,65,81)); t.setPadding(0,dp(9),0,dp(9)); resultBox.addView(t); return t; }

    void addShareRow(String name,String initial,int index){
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.VERTICAL); row.setPadding(0,dp(3),0,dp(3)); shareGrid.addView(row);
        TextView l=label(name); row.addView(l);
        TextView value=new TextView(this); value.setText(initial); value.setTextSize(19); value.setTextColor(Color.rgb(17,24,39)); value.setGravity(Gravity.CENTER_VERTICAL); value.setTypeface(Typeface.DEFAULT,Typeface.BOLD); value.setPadding(dp(13),0,dp(13),0); value.setBackground(bg(Color.WHITE,Color.rgb(229,231,235))); row.addView(value,new LinearLayout.LayoutParams(-1,dp(52)));
        if(index==0) ana=value; else if(index==1) gonda=value; else if(index==2) kora=value; else if(index==3) kranti=value; else til=value;
        value.setOnClickListener(v->showPicker(name,value,index));
    }

    void showPicker(String unit, TextView target, int index){
        String[] symbols, names;
        if(index==0){symbols=anaSymbols;names=anaNames;}
        else if(index==1){symbols=nums(20);names=names(20,unit);}
        else if(index==2){symbols=nums(4);names=names(4,unit);}
        else if(index==3){symbols=nums(3);names=names(3,unit);}
        else {symbols=nums(20);names=names(20,unit);}
        LinearLayout list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); list.setPadding(dp(8),dp(4),dp(8),dp(4));
        ScrollView sv=new ScrollView(this); sv.addView(list); AlertDialog dlg=new AlertDialog.Builder(this).setTitle(unit+" নির্বাচন করুন").setView(sv).create();
        for(int i=0;i<symbols.length;i++){ final String s=symbols[i], n=names[i]; TextView opt=new TextView(this); opt.setText(s+"     "+n); opt.setTextSize(17); opt.setTextColor(Color.rgb(17,24,39)); opt.setGravity(Gravity.CENTER_VERTICAL); opt.setPadding(dp(16),0,dp(12),0); opt.setBackground(bg(Color.WHITE,Color.rgb(229,231,235))); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(50)); p.topMargin=dp(5); list.addView(opt,p); opt.setOnClickListener(v->{target.setText(s);dlg.dismiss();}); }
        dlg.show();
    }
    String[] nums(int n){String[] a=new String[n]; for(int i=0;i<n;i++)a[i]=String.valueOf(i+1);return a;}
    String[] names(int n,String u){String[] a=new String[n];for(int i=0;i<n;i++)a[i]=(i+1)+" "+u;return a;}

    void calculate(){
        double total=parse(totalInput.getText().toString());
        if(total<=0){totalInput.setError("মোট জমির পরিমাণ লিখুন");return;}
        int a=anaIndex(ana.getText().toString()); int g=parseInt(gonda.getText().toString()); int k=parseInt(kora.getText().toString()); int c=parseInt(kranti.getText().toString()); int t=parseInt(til.getText().toString());
        long shareTil=((long)a*20*4*3*20)+((long)g*4*3*20)+((long)k*3*20)+((long)c*20)+t;
        double fraction=shareTil/76800.0; double pct=fraction*100.0; double area=total*fraction;
        resultShot.setText("শতাংশ — "+fmt(area)+" ("+fmt(pct)+"%)"); resultPct.setVisibility(View.GONE); resultKatha.setText("কাঠা — "+fmt(area/1.65)); resultBigha.setText("বিঘা — "+fmt(area/33.0)); resultAcre.setText("একর — "+fmt(area/100.0)); resultBox.setVisibility(View.VISIBLE);
    }
    int anaIndex(String s){for(int i=0;i<anaSymbols.length;i++)if(anaSymbols[i].equals(s))return i+1;return 0;}
    int parseInt(String s){try{return Integer.parseInt(s.replaceAll("[^0-9]",""));}catch(Exception e){return 0;}}
    double parse(String s){try{return Double.parseDouble(s.replace(',','.').trim());}catch(Exception e){return 0;}}
    String fmt(double x){ if(Math.abs(x)<1e-10)x=0; return String.format(Locale.US,"%.4f",x).replaceAll("0+$","").replaceAll("\\.$",""); }

    static class GradientDrawableLike extends android.graphics.drawable.GradientDrawable { GradientDrawableLike(int color,int stroke,int radius){setColor(color);setStroke(1,stroke);setCornerRadius(radius);} }
}
