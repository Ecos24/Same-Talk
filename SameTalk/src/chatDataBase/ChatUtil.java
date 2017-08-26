package chatDataBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ChatUtil
{
	public static final int ORIGINATED_ME = 21314;
	public static final int ORIGINATED_OTHER = 53432;
	
	// To display time in hh:mm:ss
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
	
	private static String css = "body\n{\n\tbackground-color: #ff9f95;\n}\n"+
							".text\n{\n\twidth:100%;\n\tdisplay:flex;\n\tflex-direction:column;\n}\n"+
							".nameheader\n{\n\twidth: 100%;\n\tmargin-top: -5px;\n\tmargin-bottom: auto;\n\tcolor: green;\n\tfont-size: 15px;\n}\n"+
							".chatmsg\n{\n\twidth:100%;\n\tmargin-top:5px;\n\tmargin-bottom:5px;\n\tline-height: 20px;\n\tfont-size: 22px;\n}\n"+
							".time\n{\n\twidth:100%;\n\ttext-align:right;\n\tcolor:silver;\n\tmargin-bottom:-5px;\n\tmargin-top: auto;\n\tfont-size: 15px;\n}\n"+
							".text-l\n{\n\tfloat:left;\n\tpadding-right:10px;\n}\n"+        
							".text-r\n{\n\tfloat:right;\n\tpadding-left:10px;\n}\n"+
							".macro\n{\n\tmargin-top:5px;\n\twidth:75%;\n\tborder-radius:5px;\n\tpadding:5px;\n\tdisplay:flex;\n}\n"+
							".msj-rta\n{\n\tfloat:right;\n\tbackground:whitesmoke;\n}\n"+
							".msj\n{\n\tfloat:left;\n\tbackground:white;\n}\n"+
							".frame\n{\n\tbackground:#e0e0de;\n\theight:88vh;\n\toverflow:hidden;\n\tpadding:0;\n}\n"+
							"ul\n{\n\twidth:98.3%;\n\theight: 82vh;\n\tlist-style-type: none;\n\tpadding:10px;\n\tbottom:0px;\n\tdisplay:flex;\n\tflex-direction: column;\n}\n"+
							".msj:before\n{\n\twidth: 0;\n\theight: 0;\n\tcontent:\"\";\n\ttop:-5px;\n\tleft:-14px;\n\tposition:relative;\n\tborder-style: solid;\n\tborder-width: 0 13px 13px 0;\n\tborder-color: transparent #ffffff transparent transparent;\n}\n"+
							".msj-rta::after\n{\n\twidth: 0;\nheight: 0;\n\tcontent:\"\";\n\ttop:-5px;\n\tleft:14px;\n\tposition:relative;\n\tborder-style: solid;\n\tborder-width: 13px 13px 0 0;\n\tborder-color: whitesmoke transparent transparent transparent;\n}\n"+  
							"::-webkit-input-placeholder\n{\n\t/* Chrome/Opera/Safari */\n\tcolor: #d4d4d4;\n}\n"+
							"::-moz-placeholder\n{\n\t/* Firefox 19+ */\n\tcolor: #d4d4d4;\n}\n"+
							":-ms-input-placeholder\n{\n\t/* IE 10+ */\n\tcolor: #d4d4d4;\n}\n"+
							":-moz-placeholder\n{\n\t/* Firefox 18- */\n\tcolor: #d4d4d4;\n}";
	
	public static void createCSS(String path) throws IOException
	{
		path += File.separator+"chatTheme.css";
		File cssFile = new File(path);
		if( !cssFile.exists() )
		{
			cssFile.createNewFile();
		}
		FileWriter fw = new FileWriter(cssFile.getAbsolutePath(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(css);
		bw.close();
		fw.close();
	}

	public static void writeRawHTML(String path, String name) throws IOException
	{
		String rawHTML = "<!DOCTYPE html>\n"+
				"<html>"+
				"\n\t<head>"+
				"\n\t\t<link rel=\"stylesheet\" href=\"chatTheme.css\">"+
				"\n\t</head>"+
				"\n\t<body>"+
				"\n\t\t<div>"+
				"\n\t\t\t<center><h2><font color=\"white\">"+name+"</font></h2></center>"+
				"\n\t\t</div>"+
				"\n\t\t<div class=\"frame\">"+
				"\n\t\t\t<ul id=\"chatlist\" style=\"overflow-y:scroll;\"></ul>"+
				"\n\t\t</div>"+        
				"\n\t</body>"+
				"\n</html>";
		
		File htmlFile = new File(path);
		if( !htmlFile.exists() )
		{
			htmlFile.createNewFile();
		}
		FileWriter fw = new FileWriter(htmlFile.getAbsolutePath(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(rawHTML);
		bw.close();
		fw.close();
	}

	// Write Chat HTML Page.
	public static void updateHTMLPersonal(String path, String msg, String receivedTime,
			int target) throws IOException
	{
		Document htmlDoc = Jsoup.parse(new File(path), "UTF-8");
		Element ul = htmlDoc.getElementById("chatlist");
		switch(target)
		{
			case ORIGINATED_ME:
				ul.append("\n\t\t\t\t<li style=\"width:100%\">"+
	                        	"\n\t\t\t\t\t\t<div class=\"msj-rta macro\">"+
                            			"\n\t\t\t\t\t\t<div class=\"text text-r\">"+
                                			"\n\t\t\t\t\t\t\t<p class=\"chatmsg\">"+msg+"</p>"+
                                			"\n\t\t\t\t\t\t\t<p class=\"time\">"+receivedTime+"</p>"+
                            			"\n\t\t\t\t\t\t</div>"+
                        		"\n\t\t\t\t\t\t</div>"+
                    		"\n\t\t\t\t</li>");
				break;
			case ORIGINATED_OTHER:
				ul.append("\n\t\t\t\t<li style=\"width:100%\">"+
                    	"\n\t\t\t\t\t\t<div class=\"msj macro\">"+
                    		"\n\t\t\t\t\t\t<div class=\"text text-r\">"+
                        		"\n\t\t\t\t\t\t\t<p>"+msg+"</p>"+
                        		"\n\t\t\t\t\t\t\t<p><small>"+receivedTime+"</small></p>"+
                    		"\n\t\t\t\t\t\t</div>"+
                		"\n\t\t\t\t\t\t</div>"+
            		"\n\t\t\t\t</li>");
				break;
		}
		System.out.println(ul);
	}
	public static void updateHTML(String path, String sendersName, String receivedTime,
			String msg, int target) throws IOException
	{
		File htmlFile = new File(path);
		Document htmlDoc = Jsoup.parse(htmlFile, "UTF-8");
		Element ul = htmlDoc.getElementById("chatlist");
		switch(target)
		{
		case ORIGINATED_ME:
			ul.append("\n\t\t\t\t<li style=\"width:100%\">"+
                        	"\n\t\t\t\t\t\t<div class=\"msj-rta macro\">"+
                        			"\n\t\t\t\t\t\t<div class=\"text text-r\">"+
                        				"\n\t\t\t\t\t\t\t<u class=\"nameheader\">"+sendersName+"</u>"+
                            			"\n\t\t\t\t\t\t\t<p class=\"chatmsg\">"+msg+"</p>"+
                            			"\n\t\t\t\t\t\t\t<p class=\"time\">"+receivedTime+"</p>"+
                        			"\n\t\t\t\t\t\t</div>"+
                    		"\n\t\t\t\t\t\t</div>"+
                		"\n\t\t\t\t</li>");
			break;
		case ORIGINATED_OTHER:
			ul.append("\n\t\t\t\t<li style=\"width:100%\">"+
                	"\n\t\t\t\t\t\t<div class=\"msj macro\">"+
                		"\n\t\t\t\t\t\t<div class=\"text text-l\">"+
        					"\n\t\t\t\t\t\t\t<u class=\"nameheader\">"+sendersName+"</u>"+
                    		"\n\t\t\t\t\t\t\t<p class=\"chatmsg\">"+msg+"</p>"+
                    		"\n\t\t\t\t\t\t\t<p class=\"time\">"+receivedTime+"</p>"+
                		"\n\t\t\t\t\t\t</div>"+
            		"\n\t\t\t\t\t\t</div>"+
        		"\n\t\t\t\t</li>");
			break;
		}
		PrintWriter outWriter = new PrintWriter(htmlFile,"UTF-8");
		outWriter.write(htmlDoc.html());
		outWriter.flush();
		outWriter.close();
		
	}
}
