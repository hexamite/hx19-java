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


class Dashboard0 {
	
	
	
	static void main(args) {
		
		
		/*
		def swing = new SwingBuilder()
		
		def sharedPanel = {
			 swing.panel() {
				label("Shared Panel")
			}
		}
		
		def count = 0
		swing.edt {
			frame(title:'HX19', defaultCloseOperation:JFrame.EXIT_ON_CLOSE, pack:true, show:true) {
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
		*/
		
		
		/*
		def mboxes = [
			[name: "root@example.com", folders: [[name: "Inbox"], [name: "Trash"]]],
			[name: "test@foo.com", folders: [[name: "Inbox"], [name: "Trash"]]]
		]
		def swing = new SwingBuilder()
		JTree mboxTree
		swing.frame(title: 'Mailer', defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
			size: [800, 600], show: true, locationRelativeTo: null) {
			lookAndFeel("system")
			menuBar() {
				menu(text: "File", mnemonic: 'F') {
					menuItem(text: "Exit", mnemonic: 'X', actionPerformed: {dispose() })
				}
			}
			splitPane {
				scrollPane(constraints: "left", preferredSize: [160, -1]) {
					mboxTree = tree(rootVisible: false)
				}
				splitPane(orientation:JSplitPane.VERTICAL_SPLIT, dividerLocation:280) {
					scrollPane(constraints: "top") { mailTable = table() }
					scrollPane(constraints: "bottom") { textArea() }
				}
			}
			["From", "Date", "Subject"].each { mailTable.model.addColumn(it) }
		}
		
		mboxTree.model.root.removeAllChildren()
		mboxes.each {mbox ->
			def node = new TreeNode(mbox.name)
			mbox.folders.each { folder -> node.add(new TreeNode(folder.name)) }
			mboxTree.model.root.add(node)
		}
		mboxTree.model.reload(mboxTree.model.root)
		*/
		
		
		
	}

}

import java.awt.BorderLayout
import java.util.EventObject;

import javax.swing.BorderFactory

/**
 * Demonstrates the use of the Groovy TableModels for viewing tables of any List of objects
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
		
		def combo = swing.comboBox(items:['one', 'two', 'three'])
		
		frame = swing.frame(title:'Hexamite hx19 Dashboard', defaultCloseOperation:JFrame.EXIT_ON_CLOSE, location:[200,200], size:[300,200]) {
			menuBar {
				menu(text: 'Help') {
					menuItem() {
						action(name: 'About', closure: { showAbout() })
					}
				}
			}
			panel {
				borderLayout()
				scrollPane(constraints:CENTER) {
					table(rowSelectionAllowed: false) {
						/*
						def model = [
							['name': 'R21', 'syncStrope': false, 'led': false], 
							['name': 'R21', 'syncStrope': false, 'led': true], 
							['name': 'R22', 'syncStrope': null, 'led': false], 
							['name': 'M10', 'syncStrope': false, 'led': false]
						]
						*/
						def model = devices.collect { device ->
							deviceProperties.keySet().collectEntries {  name ->
								 [name, device[name]]
							}
						}
						tableModel(list: model) {
							
							deviceProperties.each {  name, type ->
								closureColumn(header: name, read: {row -> return row[name]})
							}
							/*
							closureColumn(header: 'Name', read: {row -> return row.name})
							closureColumn(
								header: 'Sync Strobe',
								read: {row -> row.syncStrope},
								cellEditor: new DefaultCellEditor(combo)
							)
							closureColumn(
								header: 'Led', 
								read: {row -> row.led}, 
								// write: {row, newValue -> println "write: $row new value: $newValue" },
								// cellRenderer: new BooleanRenderer(),
								cellEditor: new DefaultCellEditor(new JCheckBox())
							)
							*/
						}
					}
				}
			}
		}
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
		  println tracker
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

class BooleanRenderer extends DefaultTableCellRenderer {
	Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		def label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
	    label.background = value ? java.awt.Color.GREEN : java.awt.Color.RED
	    label
	 }
}

