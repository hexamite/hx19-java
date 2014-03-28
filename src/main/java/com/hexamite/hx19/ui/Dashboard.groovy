package com.hexamite.hx19.ui

import groovy.swing.*
import javax.swing.*
import java.awt.*


class Dashboard {
	
	
	
	static void main(args) {
		def swing = new SwingBuilder()
		
		def sharedPanel = {
			 swing.panel() {
				label("Shared Panel")
			}
		}
		
		def count = 0
		swing.edt {
			frame(title:'Frame', defaultCloseOperation:JFrame.EXIT_ON_CLOSE, pack:true, show:true) {
				vbox {
					textlabel = label("Click the button!")
					button(
						text:'Click Me',
						actionPerformed: {
							count++
							textlabel.text = "Clicked ${count} time(s)."
							println "Clicked!"
						}
					)
					widget(sharedPanel())
					widget(sharedPanel())
				}
			}
		}
	}

}


