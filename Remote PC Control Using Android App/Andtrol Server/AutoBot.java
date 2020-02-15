import java.awt.Desktop;
import java.awt.Robot;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoBot 
{
	private Robot robot;
	private int mouseSpeed = 1;
	int lastXpos = 0;
	int lastYpos = 0;
	int lastScrollY;
	boolean specialKey = true;
    boolean bSound = true;
        
	Runtime r = Runtime.getRuntime();
    
	public AutoBot()
	{
          
            try
			{ 
                robot = new Robot(); 
            }
            catch(Exception e)
			{
                System.out.println("Robot is not created!");
            }
	}
	
	public void handleMessage(String event)
	{
            String homePath = System.getProperty("user.home");
            File userHome = new File(homePath);
            try
			{
					
				String[] divStr = event.split("!!");
					
                if(divStr[0].equals("CLICK"))
				{
                    clickMouse(divStr[1]);
				}
					
                else if(divStr[0].equals("MouseSpeed"))
				{
                    mouseSpeed = Integer.parseInt(divStr[1]);
				}
                
					
				else if(divStr[0].equals("DOWN"))
				{
                    lastXpos = Integer.parseInt(divStr[1]);
                    lastYpos = Integer.parseInt(divStr[2]);
				}
			
				else if(divStr[0].equals("UP"))
				{
               
                    int dTime = Integer.parseInt(divStr[2]) - Integer.parseInt(divStr[1]);
                    if(dTime < 250)
					{
                        clickMouse("LEFT");
                    }
				}
					
				else if(divStr[0].equals("MOVE"))
				{
                    int currXpos = Integer.parseInt(divStr[1]);
                    int currYpos = Integer.parseInt(divStr[2]);
                    int dX = (lastXpos - currXpos) * -1;
                    int dY = (lastYpos - currYpos) * -1;
						
                    moveMouse(dX, dY);
                    lastXpos = currXpos;
					lastYpos = currYpos;
				}
						
				else if(divStr[0].equals("SCROLL"))
				{
                    if(divStr[1].equals("DOWN"))
					{
                        lastScrollY = lastYpos;
                    }
                    else if(divStr[1].equals("MOVE"))
					{
						int currYpos = Integer.parseInt(divStr[3]);
						int deltaY = currYpos - lastScrollY;
						int distance = 10;
						if(deltaY > distance)
						{
										robot.mouseWheel(1);
										lastScrollY = currYpos;
						}
						if (deltaY < -1 * distance)
						{
										robot.mouseWheel(-1);
										lastScrollY = currYpos;
						}					
					}
				}
						
				else if(divStr[0].equals("KEY"))
				{
						
                    typeChar(divStr[1].charAt(0));
				}
						
				else if(divStr[0].equals("S_KEY"))
				{
                    if(specialKey)
					{
                        int key;
						int key_code = Integer.parseInt(divStr[1]);
						switch(key_code)
						{
                            case 66: key = KeyEvent.VK_ENTER; break;
                            case 67: key = KeyEvent.VK_BACK_SPACE; break;
                            default: key = -1; break;
						}
							
                        keyBoardPress(key);
                        specialKey = false;
                    }
                    else
					{
                        specialKey = true;
                    }
				}
				
                else if(divStr[0].equals("PREVIOUS"))
				{
                    keyBoardPress(KeyEvent.VK_UP);
                }
					
                else if(divStr[0].equals("PLAY"))
				{
                    keyBoardPress(KeyEvent.VK_F5);
                }
					
                else if(divStr[0].equals("NEXT"))
				{
                    keyBoardPress(KeyEvent.VK_DOWN);
                }
					
                else if(divStr[0].equals("STOP"))
				{
                    keyBoardPress(KeyEvent.VK_ESCAPE);
                }
				
				 
					
                else if(divStr[0].equals("CLOSE"))
				{
                    keyBoardPress(KeyEvent.VK_ALT, KeyEvent.VK_F4);
                }
					
                else if(divStr[0].equals("SHUTDOWN"))
				{
                    r.exec("shutdown -s");
                }
					
                else if(divStr[0].equals("RESTART"))
				{
                    r.exec("shutdown -r");
                }
					
                else if(divStr[0].equals("SWITCHUSER"))
				{
                    keyBoardPress(KeyEvent.VK_ALT, KeyEvent.VK_TAB);
                }
				
                else if(divStr[0].equals("LOGOUT"))
				{
                    r.exec("shutdown -l");
                }
				
                else if(divStr[0].equals("HIBERNATE"))
				{
                    r.exec("shutdown -h");
                }
					
                else if(divStr[0].equals("SLEEP"))
				{
                    r.exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
                }
					
                else if(divStr[0].equals("MUTESOUND"))
				{
                    if(bSound)
					{
                        keyBoardPress(KeyEvent.VK_WINDOWS, KeyEvent.VK_X);
                        bSound = false;
                    }
                    else
					{
                        keyBoardPress(KeyEvent.VK_TAB);
                        keyBoardPress(KeyEvent.VK_TAB);
                        keyBoardPress(KeyEvent.VK_TAB);
                        keyBoardPress(KeyEvent.VK_ENTER);
                        keyBoardPress(KeyEvent.VK_ALT, KeyEvent.VK_F4);
                        bSound = true;
                    }
                }
					
                else if(divStr[0].equals("SELECTALL"))
				{
                    keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_A);
                }
				else if(divStr[0].equals("SAVE"))
				{
                    keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_S);
                }
				
                else if(divStr[0].equals("DELETEBT"))
				{
                    keyBoardPress(KeyEvent.VK_DELETE);
                }
					
				
                else if(divStr[0].equals("OPENCOMPUTER"))
				{
                    keyBoardPress(KeyEvent.VK_WINDOWS, KeyEvent.VK_E);
					
                }
					
                else if(divStr[0].equals("OPENDOCUMENTS"))
				{
                    File documents = new File(userHome, "Documents");
                    openFolder(documents);
                }
					
                else if(divStr[0].equals("OPENDESKTOP"))
				{
                    File desktop = new File(userHome, "Desktop");
                    openFolder(desktop);
                }
					
                else if(divStr[0].equals("OPENMUSIC"))
				{
                    File music = new File(userHome, "Music");
                    openFolder(music);
                }
					
                else if(divStr[0].equals("OPENVIDEO"))
				{
                    File video = new File(userHome, "Videos");
                    openFolder(video);
                }
					
                else if(divStr[0].equals("OPENIMAGE"))
				{
                    File picture = new File(userHome, "Pictures");
                    openFolder(picture);
                }
					
                else if(divStr[0].equals("OPENDOWNLOAD"))
				{
                    File download = new File(userHome, "Downloads");
                    openFolder(download);
                }
					
                else if(divStr[0].equals("OPENLINKS"))
				{
                    File links = new File(userHome, "Links");
                    openFolder(links);
                }
					
                else if(divStr[0].equals("OPENCONTACTS"))
				{
                    File contacts = new File(userHome, "Contacts");
                    openFolder(contacts);
                }
					
                else if(divStr[0].equals("OPENFAVORITE"))
				{
                    File favorites = new File(userHome, "Favorites");
                    openFolder(favorites);
                }
					
                else if(divStr[0].equals("UPBUTTON"))
				{
                    keyBoardPress(KeyEvent.VK_UP);
                }
				
                else if(divStr[0].equals("OPENRECENT"))
				{
                    File recent = new File(userHome, "Recent");
                    openFolder(recent);
                }
					
                else if(divStr[0].equals("LEFTBUTTON"))
				{
                    keyBoardPress(KeyEvent.VK_LEFT);
                }
				
                else if(divStr[0].equals("ENTER"))
				{
                    keyBoardPress(KeyEvent.VK_ENTER);
                }
				else if(divStr[0].equals("ENTER1"))
				{
                    keyBoardPress(KeyEvent.VK_ENTER);
                }
				
                else if(divStr[0].equals("RIGHTBUTTON"))
				{
                    keyBoardPress(KeyEvent.VK_RIGHT);
                }
					
                else if(divStr[0].equals("DOWNBUTTON"))
				{
                    keyBoardPress(KeyEvent.VK_DOWN);
                }
					
                else if(divStr[0].equals("CALCULATOR"))
				{
                    r.exec("calc");
                }
					
                else if(divStr[0].equals("NOTEPAD"))
				{
                    r.exec("notepad");
                }
					
                else if(divStr[0].equals("CMD"))
				{
                  
					 Runtime.getRuntime().exec("cmd.exe /c start");
                }
					
                else if(divStr[0].equals("PAINT"))
				{
                     r.exec("mspaint");
                    
                }
					
                else if(divStr[0].equals("BACK"))
				{
                     keyBoardPress(KeyEvent.VK_ALT, KeyEvent.VK_LEFT);
                }
					
                else if(divStr[0].equals("SNIPPINGTOOLS"))
				{
                     r.exec("SnippingTool");
                }
					
                else if(divStr[0].equals("MINIMIZE"))
				{
                     keyBoardPress(KeyEvent.VK_WINDOWS, KeyEvent.VK_D);
                }
					
                else if(divStr[0].equals("FORWARD"))
				{
                     keyBoardPress(KeyEvent.VK_ALT, KeyEvent.VK_RIGHT);
                }

					
                else if(divStr[0].equals("CLOSEMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_ALT, KeyEvent.VK_F4);
                }
					
                else if(divStr[0].equals("LISTMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_L);
                }
					
                else if(divStr[0].equals("MUTEMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_M);
                }
					
                else if(divStr[0].equals("REWINDMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_LEFT);
                }
					
                else if(divStr[0].equals("PLAYMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_SPACE);
					 
                }
				
                else if(divStr[0].equals("FORWARDMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_RIGHT);
                }
					
                else if(divStr[0].equals("PREVIOUSMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_P);
					 
                }
					
                else if(divStr[0].equals("STOPMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_S);
                }
					
                else if(divStr[0].equals("NEXTMEDIA"))
				{
					keyBoardPress(KeyEvent.VK_N);
                }
					
                else if(divStr[0].equals("INCREASEMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_UP);
                }
					
                else if(divStr[0].equals("DECREASEMEDIA"))
				{
                     keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_DOWN);
                }
				
  
                else if(divStr[0].equals("PREVIOUSIMAGE"))
				{
                     keyBoardPress(KeyEvent.VK_LEFT);
                }
					
                else if(divStr[0].equals("NEXTIMAGE"))
				{
                     keyBoardPress(KeyEvent.VK_RIGHT);
                }
					
                else if(divStr[0].equals("ROTATESAME"))
				{
                     keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_PERIOD);
                }
					
                else if(divStr[0].equals("ROTATEVERSUS"))
				{
                     keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_COMMA);
                }
					
                else if(divStr[0].equals("DELETEIMAGE"))
				{
                     keyBoardPress(KeyEvent.VK_DELETE);
                }
					
                else if(divStr[0].equals("CLOSEIMAGE"))
				{
                     keyBoardPress(KeyEvent.VK_ALT, KeyEvent.VK_F4);
                }
				
				 else if(divStr[0].equals("PRINT"))
				{
                     keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_P);
                }
				else if(divStr[0].equals("WINDOWS"))
				{
                     
					 keyBoardPress(KeyEvent.VK_WINDOWS);
                }
				
				else if(divStr[0].equals("NEW"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_N);
                }
				else if(divStr[0].equals("NEW1"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_N);
                }
				else if(divStr[0].equals("OPEN"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_O);
                }
				else if(divStr[0].equals("EDIT"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_E);
                }
				else if(divStr[0].equals("CUT"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_X);
                }
				else if(divStr[0].equals("COPY"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_C);
                }
				else if(divStr[0].equals("PASTE"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_V);
                }
				else if(divStr[0].equals("CLOSE"))
				{
                     
					 keyBoardPress(KeyEvent.VK_ALT, KeyEvent.VK_F4);
                }
				else if(divStr[0].equals("UNDO"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL, KeyEvent.VK_Z);
                }
				else if(divStr[0].equals("SAVEAS"))
				{
                     
					 keyBoardPress(KeyEvent.VK_F12);
                }
				else if(divStr[0].equals("TAB"))
				{
                     
					 keyBoardPress(KeyEvent.VK_CONTROL,KeyEvent.VK_ALT,KeyEvent.VK_TAB);
					
                }
			
                else
				{
                    System.out.println("ERROR MESSAGE" + divStr[0] + " " + divStr[1]);
                }
			}
			catch(Exception e){}
	}
      
        public void openFolder(File nFolder)
		{
            try 
			{
				Desktop.getDesktop().open(nFolder);
            } 
			catch (IOException ex) {}
        }
      
		public void moveMouse(int x, int y)
		{
			
			int cur_x = MouseInfo.getPointerInfo().getLocation().x;
			int cur_y = MouseInfo.getPointerInfo().getLocation().y;
			
			robot.mouseMove(cur_x + x * mouseSpeed, cur_y + y * mouseSpeed);
		}
	
        public void scrollMouseWheel(int notches)
		{
			robot.mouseWheel(notches);
		}

        public void clickMouse(String leftStr)
		{
			int button;
				
			if(leftStr.equals("LEFT"))
			{
				button = InputEvent.BUTTON1_MASK;
			}
				
					else
					{ 
						button = InputEvent.BUTTON3_MASK;
					}
					
			robot.mousePress(button);
			robot.mouseRelease(button);
		}
	
	
	public void typeChar(char character) 
	{
        switch (character) 
		{
            case 'a': keyBoardPress(KeyEvent.VK_A); break;
            case 'b': keyBoardPress(KeyEvent.VK_B); break;
            case 'c': keyBoardPress(KeyEvent.VK_C); break;
            case 'd': keyBoardPress(KeyEvent.VK_D); break;
            case 'e': keyBoardPress(KeyEvent.VK_E); break;
            case 'f': keyBoardPress(KeyEvent.VK_F); break;
            case 'g': keyBoardPress(KeyEvent.VK_G); break;
            case 'h': keyBoardPress(KeyEvent.VK_H); break;
            case 'i': keyBoardPress(KeyEvent.VK_I); break;
            case 'j': keyBoardPress(KeyEvent.VK_J); break;
            case 'k': keyBoardPress(KeyEvent.VK_K); break;
            case 'l': keyBoardPress(KeyEvent.VK_L); break;
            case 'm': keyBoardPress(KeyEvent.VK_M); break;
            case 'n': keyBoardPress(KeyEvent.VK_N); break;
            case 'o': keyBoardPress(KeyEvent.VK_O); break;
            case 'p': keyBoardPress(KeyEvent.VK_P); break;
            case 'q': keyBoardPress(KeyEvent.VK_Q); break;
            case 'r': keyBoardPress(KeyEvent.VK_R); break;
            case 's': keyBoardPress(KeyEvent.VK_S); break;
            case 't': keyBoardPress(KeyEvent.VK_T); break;
            case 'u': keyBoardPress(KeyEvent.VK_U); break;
            case 'v': keyBoardPress(KeyEvent.VK_V); break;
            case 'w': keyBoardPress(KeyEvent.VK_W); break;
            case 'x': keyBoardPress(KeyEvent.VK_X); break;
            case 'y': keyBoardPress(KeyEvent.VK_Y); break;
            case 'z': keyBoardPress(KeyEvent.VK_Z); break;
            case 'A': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_A); break;
            case 'B': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_B); break;
            case 'C': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_C); break;
            case 'D': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_D); break;
            case 'E': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_E); break;
            case 'F': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_F); break;
            case 'G': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_G); break;
            case 'H': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_H); break;
            case 'I': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_I); break;
            case 'J': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_J); break;
            case 'K': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_K); break;
            case 'L': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_L); break;
            case 'M': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_M); break;
            case 'N': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_N); break;
            case 'O': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_O); break;
            case 'P': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_P); break;
            case 'Q': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_Q); break;
            case 'R': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_R); break;
            case 'S': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_S); break;
            case 'T': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_T); break;
            case 'U': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_U); break;
            case 'V': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_V); break;
            case 'W': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_W); break;
            case 'X': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_X); break;
            case 'Y': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_Y); break;
            case 'Z': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_Z); break;
            case '`': keyBoardPress(KeyEvent.VK_BACK_QUOTE); break;
            case '0': keyBoardPress(KeyEvent.VK_0); break;
            case '1': keyBoardPress(KeyEvent.VK_1); break;
            case '2': keyBoardPress(KeyEvent.VK_2); break;
            case '3': keyBoardPress(KeyEvent.VK_3); break;
            case '4': keyBoardPress(KeyEvent.VK_4); break;
            case '5': keyBoardPress(KeyEvent.VK_5); break;
            case '6': keyBoardPress(KeyEvent.VK_6); break;
            case '7': keyBoardPress(KeyEvent.VK_7); break;
            case '8': keyBoardPress(KeyEvent.VK_8); break;
            case '9': keyBoardPress(KeyEvent.VK_9); break;
            case '-': keyBoardPress(KeyEvent.VK_MINUS); break;
            case '=': keyBoardPress(KeyEvent.VK_EQUALS); break;
            case '~': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE); break;
            case '!': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_1); break;
			case '@': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_2); break;
            case '#': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_3); break;
            case '$': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_4); break;
            case '%': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_5); break;
            case '^': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_6); break;
            case '&': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_7); break;
            case '*': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_8); break;
            case '(': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_9); break;
            case ')': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_0); break;
            case '_': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_MINUS); break;
            case '+': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_EQUALS); break;
            case '\t': keyBoardPress(KeyEvent.VK_TAB); break;
            case '\n': keyBoardPress(KeyEvent.VK_ENTER); break;
            case '[': keyBoardPress(KeyEvent.VK_OPEN_BRACKET); break;
            case ']': keyBoardPress(KeyEvent.VK_CLOSE_BRACKET); break;
            case '\\': keyBoardPress(KeyEvent.VK_BACK_SLASH); break;
            case '{': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET); break;
            case '}': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET); break;
            case '|': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH); break;
            case ';': keyBoardPress(KeyEvent.VK_SEMICOLON); break;
            case ':': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON); break;
            case '\'': keyBoardPress(KeyEvent.VK_QUOTE); break;
            case '"': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE); break;
            case ',': keyBoardPress(KeyEvent.VK_COMMA); break;
            case '<': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA); break;
            case '.': keyBoardPress(KeyEvent.VK_PERIOD); break;
            case '>': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD); break;
            case '/': keyBoardPress(KeyEvent.VK_SLASH); break;
            case '?': keyBoardPress(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH); break;
            case ' ': keyBoardPress(KeyEvent.VK_SPACE); break;
            default: System.out.println("Character " + character); break;
        }
    }
	
	public void keyBoardPress(int key)
	{
            try
			{
               
                robot.keyPress(key);
				robot.keyRelease(key);
            }
			catch(Exception e){}
	}
       
	public void keyBoardPress(int key, int key2)
	{
            try
			{
                robot.keyPress(key);
                robot.keyPress(key2);
                robot.keyRelease(key2);
                robot.keyRelease(key);
            }
			catch(Exception e){}
	}
        
	public void keyBoardPress(int key, int key2, int key3)
	{
            try
			{
                robot.keyPress(key);
                robot.keyPress(key2);
                robot.keyPress(key3);
                robot.keyRelease(key3);
                robot.keyRelease(key2);
                robot.keyRelease(key);
            }
			catch(Exception e){}
	}
}
