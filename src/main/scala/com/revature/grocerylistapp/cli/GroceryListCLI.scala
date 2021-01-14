package com.revature.grocerylistapp.cli

import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine
import scala.io.Source
import java.io.FileNotFoundException
import scala.collection.mutable.Map
import scala.io.AnsiColor
import com.revature.grocerylistapp.util.FileUtil

class GroceryListCLI extends AnsiColor{
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
            val input = readLine(s"${BLINK}What would you like to do? ${RESET}\n" +
            s"\n${BLUE}${BOLD}ADD${RESET} an item" +
            s"\n${BLUE}${BOLD}CHANGE${RESET} an item" +
            s"\n${BLUE}${BOLD}DELETE${RESET} an item" +
            s"\n${BLUE}${BOLD}IMPORT${RESET} a list" +
            s"\n${BLUE}${BOLD}EXIT${RESET}" +
            "\n--")


            input.toUpperCase match {
                case "ADD" => addItem()
                case "CHANGE" => changeItem()     
                case "DELETE" => deleteItem()
                case "IMPORT" => appendListWithImport()
                case "EXIT" => exitApp()
                case _ => println("\n* You did not enter a valid input. *\n")     
            }
 
        }
    }

    def printList() {
        println(s"\n         ${GREEN}${BOLD}GROCERY LIST\n──────────────────────────────\nItem Name     :     Department\n${RESET}")
        // for (a <- 1 to itemNamesArray.length)
        //     println(s"$a. ${itemNamesArray(a-1)}")
        for ((i,t) <- itemNamesAndTagsMap) println(s"${i}     :     ${t}")
        if (itemNamesAndTagsMap.size == 0) {
            println(s"${WHITE}         [List Empty]${RESET}")
        }
        println(s"${GREEN}${BOLD}──────────────────────────────\n${RESET}")

    }

    def addItem() {
        var keepAdding = true
        while (keepAdding == true) {
            var add = readLine(s"\nType the name of an item to add, or ${BLUE}${BOLD}BACK${RESET} to go back: \n--").toUpperCase()
            add match {
                case "BACK" | "EXIT" | "QUIT" | "STOP" => keepAdding = false
                case _ => itemNamesAndTagsMap += (add -> getTag(add))
                println(s"\n$add : ${itemNamesAndTagsMap(add)}\n")
                printList()
            }     
        }
    }

    def getTag(itemToGetTagFor: String): String = {
        itemToGetTagFor match {
            case "STEAK" | "BEEF" | "CHICKEN" | "FISH" | "TILAPIA" | "PORK" => "Meat / Seafood"
            case "BANANAS" | "APPLES" | "CUCUMBERS" => "Fresh Produce"
            case "CINNAMON" | "SALT" | "PAPRIKA" => "Spices"
            case _ => "Misc."
        }
    }

    // What functionality SHOULD occur above:
    // Create itemNamesAndTags Map
    // Match value of 'add' to other Map of pre-defined values
    // itemNamesArray += (item -> )

    def changeItem() {
        var itemChangeFrom = readLine(s"\nType the name of the item you want to change, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()
           
        if (itemNamesAndTagsMap.contains(itemChangeFrom)) {
            var itemChangeTo = readLine(s"\nType what you'd like to change ${itemChangeFrom} to, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()
            itemNamesAndTagsMap -= (itemChangeFrom)
            itemNamesAndTagsMap += (itemChangeTo -> getTag(itemChangeTo))
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
