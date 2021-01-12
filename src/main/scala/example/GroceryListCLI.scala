package example
import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine
import scala.io.Source
import java.io.FileNotFoundException


class GroceryListCLI{
    var itemNamesArray = ArrayBuffer[String]("JALAPENOS", "ONIONS", "CARROTS") //Starting list before db implementation
    var continueUsingList = false

    def useList() {
        continueUsingList = true
        while (continueUsingList) {
            printList()
            val input = readLine("What would you like to do? \n" +
            "\nType \"1\" to ADD an item" +
            "\nType \"2\" to CHANGE an item" +
            "\nType \"3\" to DELETE an item" +
            "\nType \"4\" to IMPORT a list" +
            "\nType \"5\" to EXIT" +
            "\n--")

            try{
                input.toInt match {
                    case 1 => addItem()
                    case 2 => changeItem()     
                    case 3 => deleteItem()
                    case 4 => appendListWithImport()
                    case 5 => exitApp()
                    case _ => println("\n* You did not enter a valid input. *\n")     
                }
            }   catch {
                    case nfe: NumberFormatException => println("\nYou did not enter a number.\n")
                            useList()
                }
        }
    }

    def appendListWithImport () {
        var file = readLine("What is the name of the file you want to import? \n")
            
        try{
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
            println(s"\n*** You've added: ${openFile.mkString} ***")
        } catch {
            case fnfe: FileNotFoundException => println("\n * This file could not be found. *")
                var tryAgain = readLine("\nWould you like to try again? YES/NO?\n").toUpperCase()
                if (tryAgain == "YES" || tryAgain == "Y") {
                    appendListWithImport()
                } else if (tryAgain == "NO" || tryAgain == "N") {
                    useList()
                } else {
                    println("\n* That was not a valid input. Going to main menu. *\n")
                }
        }          
    }

    def printList() {
        println("\nGROCERY LIST:\n────────────────────")
        for (a <- 1 to itemNamesArray.length)
            println(s"$a. ${itemNamesArray(a-1)}")
        println("────────────────────\n")
    }

    def addItem() {
        var add = readLine("Type the NAME of the item you'd like to add: \n--").toUpperCase()
        itemNamesArray += add
        println(s"\n❋❋❋ You've added ${add} to your grocery list. ❋❋❋")
    }

    def deleteItem() {
        var delete = readLine("Type the NAME of the item you'd like to delete: \n").toUpperCase()

        if (itemNamesArray.contains(delete)) {
            println(delete)
            itemNamesArray -= delete
            println(s"\n❋❋❋ You've deleted ${delete} from your grocery list. ❋❋❋")
        }
        else {
            println("\n* This is ALREADY NOT on your list. *")
        }
    }

    def changeItem() {
        var itemChangeFrom = readLine("\nType the NAME of the item you want to change: \n").toUpperCase()
            if (itemNamesArray.contains(itemChangeFrom)) {
                var itemChangeTo = readLine(s"\nType what you'd like to change $itemChangeFrom to: \n").toUpperCase()
                var indexToChange = itemNamesArray.indexOf(itemChangeFrom)
                itemNamesArray(indexToChange) = itemChangeTo
                println(s"\n❋❋❋ You changed $itemChangeFrom to $itemChangeTo. ❋❋❋")
            } else {
                println("\n❋ This item does not exist in your list. ❋")
            }
    }

    def exitApp() {
        println("Goodbye!")
        continueUsingList = false
    }
}
