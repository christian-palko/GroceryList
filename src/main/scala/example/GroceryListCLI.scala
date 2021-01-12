package example
import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine
import scala.io.Source


class GroceryListCLI{
    var itemNamesArray = ArrayBuffer[String]("JALAPENOS", "ONIONS", "CARROTS")
    def useList() {
        var continueUsingList = true
        while (continueUsingList) {
            printList()
            var add, change1, change2, delete = ""
            val input = readLine("What would you like to do? \n" +
            "Type \"1\" to ADD an item" +
            "\nType \"2\" to CHANGE an item" +
            "\nType \"3\" to DELETE an item" +
            "\nType \"4\" to IMPORT a list" +
            "\nType \"5\" to EXIT" +
            "\n--")

            try{
                input.toInt match {
                    case 1 => add = readLine("Type the NAME of the item you'd like to add: \n--")
                        addItem(add.toUpperCase())
                    
                    case 2 => change1 = readLine("Type the NUMBER of the item you want to change: \n")
                        if (change1.toInt > 0 & change1.toInt <= itemNamesArray.length) {
                            change2 = readLine(s"Type the NAME you want to change $change1 to: \n")
                            changeItem(change1.toInt, change2.toUpperCase())
                        }
                        
                    case 3 => deleteItem()

                    case 4 => appendListWithImport()

                    case 5 => println("Goodbye!")
                        continueUsingList = false
                        
                    case _ => println("You did not enter a valid input.\n")
                        useList()
                }
            }   catch {
                    case e: NumberFormatException => println("\nYou did not enter a number.\n")
                            useList()
                }
        }
    }

    def appendListWithImport () {
        var file = readLine("What is the name of the file you want to import? \n")
            val openFile = FileUtil.importFile(file)
            val openFileAdd = openFile
                .toUpperCase
                .replaceAll(" ", "")
                .trim.split(",")
                .to(ArrayBuffer)
            for (item <- openFileAdd) {
            println(item)
            itemNamesArray += item
            }
            println(s"\n You've added: ${openFile.mkString}:")
                    
    }

    def printList() {
        println("\nGROCERY LIST:\n────────────────────")
        for (a <- 1 to itemNamesArray.length)
            println(s"Item $a: ${itemNamesArray(a-1)}")
        println("────────────────────\n")
    }

    def addItem(item : String) {
        itemNamesArray += item
        println(s"\n❋❋❋ You've added ${item} to your grocery list. ❋❋❋")
    }

    def deleteItem() {
        var delete = readLine("Type the NAME of the item you'd like to delete: \n").toUpperCase()

        if (itemNamesArray.contains(delete)) {
            println(delete)
            itemNamesArray -= delete
            println(s"\n❋❋❋ You've deleted ${delete} from your grocery list. ❋❋❋")
        }
        else {
            println("\n --- This is ALREADY NOT on your list. --- ")
        }
    }


    def changeItem(itemNum : Int, item : String) {
        
        if(itemNum <= itemNamesArray.length)  { 
            itemNamesArray(itemNum-1) = item
            println(s"\n❋❋❋ You've changed Item ${itemNum} to ${item} ❋❋❋")
        } else {
            println(s"There are not $itemNum items on your list.")
        }
    }


}
