package com.hexamite.hx19.ui

import com.hexamite.hx19.device.*
import groovy.swing.*
import javax.swing.*

import java.awt.*

import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableCellEditor
import javax.swing.tree.DefaultMutableTreeNode as TreeNode
import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import java.util.EventObject;
import javax.swing.BorderFactory

/**
 *
 */
class Dashboard {
	
	def frame
	def swing
	
	static void main(args) {
		def dashboard = new Dashboard()
		dashboard.run()
	}
	
	void run() {
		
		/*
		final JCheckBox checkBox = new JCheckBox();
		jTable1.getColumn("Include").setCellRenderer(new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
			  check.setSelected(((Boolean)value).booleanValue()) ;
			  return check;
			}
		});
		*/
		
		Tracker tracker = config()
		def devices = tracker.monitors + tracker.soundEmitters + tracker.soundListeners
				
		swing = new SwingBuilder()
		
		def combo = swing.comboBox(items: ['one', 'two', 'three'])

        JTextArea messages = null
        JTextField message = null

		frame = swing.frame(title: 'Hexamite hx19 Dashboard', defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
			menuBar {
				menu(text: 'Help') {
					menuItem() {
						action(name: 'About', closure: { showAbout() })
					}
				}
			}
            borderLayout()
            scrollPane(constraints: NORTH) {
                table(rowSelectionAllowed: false) {
                    def model = devices.collect { device ->
                        deviceProperties.keySet().collectEntries { name ->
                            [name, device[name]]
                        }
                    }
                    tableModel(list: model, ) {
                        deviceProperties.each { name, type ->
                            closureColumn(header: name, read: { row -> row[name] })
                        }
                    }
                }
            }
            scrollPane(constraints: CENTER) {
                messages = textArea(id: 'TextArea', lineWrap: false, editable: true)
            }
            panel(constraints: SOUTH){
                borderLayout()
                label(constraints: WEST, text: 'Message: ')
                message = textField(constraints: CENTER)
                button(constraints: EAST, text: 'Send', actionPerformed: { messages.append(message.text + '\n') })
            }
		}
        frame.size = [frame.width, 600]
        frame.pack()
		frame.show()
	}
	
	void showAbout() {
		 def pane = swing.optionPane(message: 'Shows the state of configured devices.')
		 def dialog = pane.createDialog(frame, 'About hx19 Dashboard')
		 dialog.show()
	}
	
	static def config() {
		def text = ('config' as File).text
		def builder = new ObjectGraphBuilder()
		def tracker
		builder.classNameResolver = "com.hexamite.hx19.device"
		try {
		  Closure closure = new GroovyShell().evaluate(text)
		  tracker = builder.tracker(closure)
		} catch(Exception e) {
			System.err.println e
			System.exit 1
		}
		tracker
	}
	
	/**
	 * Determine name and class of all device properties.
	 * */
	Map getDeviceProperties() {
		return [
			name : String,
			version : String,
			syncStrobe : Boolean,
			acquisitionRate : Integer,
			batteryStatus : Integer,
			firstTagInQueue : Integer,
			deepSleep : Boolean,
			monitorBattery : Boolean,
			countRecords : Boolean,
			led : Boolean,
			noiceRecovery : Boolean,
			directNetworkAccess : Boolean,
			powerSavings : Boolean,
			serialPin : Boolean,
			doppler : Boolean,
			rfid : Boolean,
			signalPower : Integer,
			inputChannel : Integer,
			numTags : Boolean,
			outputChannel : Integer,
			workRegisters : Boolean
		]
	}

}

/*
class BooleanRenderer extends DefaultTableCellRenderer {
	Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		def label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
	    label.background = value ? java.awt.Color.GREEN : java.awt.Color.RED
	    label
	 }
}
*/