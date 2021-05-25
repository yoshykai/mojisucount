import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class Main{
  public static void main(String args[]){
    JFrame app = new JFrame();
    app.add(new Draw());
    app.setSize(700,400);
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    app.setVisible(true);
  }
}

class Draw extends JPanel{
  JTextArea ta;
  JButton save,load;
  JTextField name;
  int count=0;
  public Draw(){
    setLayout(null);
    ta = new JTextArea(10,50);
    ta.setLineWrap(true);
    JScrollPane sp = new JScrollPane(ta);
    sp.setBounds(5,5,650,130);
    name = new JTextField(); name.setBounds(5,200,200,30);
    save = new JButton("save");save.setBounds(5,150,70,30);
    load = new JButton("load");load.setBounds(90,150,70,30);
    this.add(sp);this.add(name);this.add(save);this.add(load);

    new javax.swing.Timer(30,new ActionListener(){
      public void actionPerformed(ActionEvent evt){
        repaint();
      }
    }).start();
    new javax.swing.Timer(60000,new ActionListener(){
      public void actionPerformed(ActionEvent evt){
        String str = ta.getText();
        FileRW.output("tmp-"+count+".txt",str);
        count=(count+1)%10;
      }
    }).start();
    save.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
        String str = ta.getText(),n=name.getText()+".txt";
        FileRW.output(n,str);
      }
    });
    load.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
        String n=name.getText()+".txt";
        String str = FileRW.input(n);
        ta.setText(str);
      }
    });

    setOpaque(false);
  }

  public void paintComponent(Graphics g){
    g.setColor(Color.black);
    g.drawString(String.valueOf(ta.getText().length()), 300, 300);
	}
}

class FileRW{
  public static File makeFile(String str){
    return new File(str);
  }

  public static String input(String name){
    try{
      File file = makeFile(name);
      if(cheakReadFile(file)){
        BufferedReader brf = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String str=brf.readLine();
        while(str!=null){
          sb.append(str+"\n");
          str = brf.readLine();
        }
        brf.close();
        return sb.toString();
      }else{
        System.out.println("ファイルが存在しないか開けません");
        System.exit(0);
      }
    }catch(IOException e){
      e.printStackTrace();
      System.exit(0);
    }
    return null;
  }

  public static void output(String name,String str){
    try{
      File file = makeFile(name);
      if(cheakWriteFile(file)){
        BufferedWriter bwf = new BufferedWriter(new FileWriter(file));
        bwf.write(str);
        bwf.newLine();
        bwf.close();
      }else{
        System.out.println("ファイルが開けません");
        System.exit(0);
      }
    }catch(IOException e){
      e.printStackTrace();
      System.exit(0);
    }
  }

  private static boolean cheakReadFile(File file){
    if (file.exists()){
      if (file.isFile() && file.canRead()){
        return true;
      }
    }
    return false;
  }

  private static boolean cheakWriteFile(File file){
    if (file.exists()){
      if (file.isFile() && file.canWrite()){
        return true;
      }
    }else{
      try{
        if(file.createNewFile()){
          return cheakWriteFile(file);
        }
      }catch(IOException e){
        e.printStackTrace();
      }
    }
    return false;
  }
}
