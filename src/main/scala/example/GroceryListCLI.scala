package example
import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine
import scala.io.Source
import java.io.FileNotFoundException
import scala.collection.mutable.Map

class GroceryListCLI{
    var itemNamesArray = ArrayBuffer[String]() //Starting list before db implementation
    var itemNamesAndTagsMap = collection.mutable.Map[String, String]()
    var continueUsingList = false

    // Add tagging system that auto-tags common items based on grocery store sections to avoid
    // having to go to any section in the store more than once. 
    //
    // Nice to have: Unknown default tag
    // Nice to have: Users can modify tags

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

    def printList() {
        println("\nGROCERY LIST:\n────────────────────")
        // for (a <- 1 to itemNamesArray.length)
        //     println(s"$a. ${itemNamesArray(a-1)}")
        for ((i,t) <- itemNamesAndTagsMap) println(s"$i : $t")
        if (itemNamesAndTagsMap.size == 0) {
            println("[LIST EMPTY]")
        }
        println("────────────────────\n")

    }

    def addItem() {
        var keepAdding = true
        while (keepAdding == true) {
        var add = readLine("\nType the NAME of the item you'd like to add, or EXIT to go back: \n--").toUpperCase()
        add match {
            case "EXIT" | "QUIT" => keepAdding = false
            case _ => itemNamesAndTagsMap += (add -> getTag(add))
            println(s"\n$add : ${itemNamesAndTagsMap(add)}\n")
            printList()
        }     
        }
    }

    def getTag(itemToGetTagFor: String): String = {
        itemToGetTagFor match {
            case "STEAK" | "BEEF" | "CHICKEN" | "FISH" | "TILAPIA" | "PORK" => return "Meat / Seafood"
            case "BANANAS" | "APPLES" | "CUCUMBERS" => return "Fresh Produce"
            case "CINNAMON" | "SALT" | "PAPRIKA" => return "Spices"
            case _ => return "Miscellaneous"
        }
    }

    // What functionality SHOULD occur above:
    // Create itemNamesAndTags Map
    // Match value of 'add' to other Map of pre-defined values
    // itemNamesArray += (item -> )

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

    def appendListWithImport () {
        var file = readLine("\nWhat is the name of the file you want to import? \n")
            
        try{
        val openFile = FileUtil.importFile(file)
            val openFileAdd = openFile
                .toUpperCase
                .replaceAll(" ", "")
                .trim.split(",")
                .to(ArrayBuffer)
            for (item <- openFileAdd) {
             /* to do - if list already contains, add prompt to pass over or add duplicate */
            if(itemNamesArray.contains(item)) {
                var alreadyContains = readLine(s"\nThis list already contains $item.\n" +
                  "Ignore this item or create a duplicate?\nType IGNORE or DUPLICATE.\n").toUpperCase()
                if (alreadyContains == "IGNORE") {
                    
                } else if (alreadyContains == "DUPLICATE") {
                    itemNamesArray += item
                }
            } else {
                itemNamesArray += item
            }
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

    def exitApp() {
        println("Goodbye!")
        continueUsingList = false
    }
}
