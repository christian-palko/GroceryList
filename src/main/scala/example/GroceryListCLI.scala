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
                        
                    case 3 => delete = readLine("Type the NAME of the item you'd like to delete: \n")
                        deleteItem(delete.toUpperCase())

                    case 4 => var openedFile = readLine("Type the CSV file you want to import: \n")
                        FileUtil.getCsvContent(openedFile, " ")

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

    def deleteItem(item : String) {
        if (itemNamesArray.contains(item.toUpperCase())) {
            itemNamesArray -= item
            println(s"\n❋❋❋ You've deleted ${item} from your grocery list. ❋❋❋")
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
