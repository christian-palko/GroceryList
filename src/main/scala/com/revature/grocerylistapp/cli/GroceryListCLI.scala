package com.revature.grocerylistapp.cli

import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine
import scala.io.Source
import java.io.FileNotFoundException
import scala.collection.mutable.Map
import scala.io.AnsiColor
import com.revature.grocerylistapp.util.FileUtil
import com.revature.grocerylistapp.util.ConnectionUtil
import scala.util.Using
import com.revature.grocerylistapp.model.Item
import com.revature.grocerylistapp.dao.ItemDao

class GroceryListCLI extends AnsiColor{
  
    var itemNamesAndTagsMap = collection.mutable.Map[String, String]()

    // WHAT I NEED TO DO: Map above starts empty. In the useList method, I need to call on a yet-to-exist method which returns the resultset
    // from one my database containing one table which has one column

    var continueUsingList = false

    

    def useList() {
        continueUsingList = true
        while (continueUsingList) {

            printList()
            val input = readLine(s"${BLINK}What would you like to do? ${RESET}\n" +
            s"\n${BLUE}${BOLD}ADD${RESET} items" +
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
        itemNamesAndTagsMap.clear()
        var retrievedList = ItemDao.getAll().to(ArrayBuffer)
        
        for (i <- retrievedList) {
            val itemParsed = i.item.toUpperCase
            itemNamesAndTagsMap += (itemParsed -> (getTag(itemParsed)))
            }

        println(s"\n         ${GREEN}${BOLD}GROCERY LIST\n──────────────────────────────\nItem Name     :     Department\n${RESET}")
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
                case _ => ItemDao.addNew(add)
                printList()
                println(s"\nAdded: $add\n")
            }     
        }
    }

    def deleteItem() {
        var delete = readLine(s"\nType the name of the item you want to delete, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()

        if (itemNamesAndTagsMap.contains(delete)) {
            println(delete)

            ItemDao.deleteFromDB(delete)
            println(s"\n❋❋❋ You've deleted ${delete} from your grocery list. ❋❋❋")
        }
        else {
            println("\n* This item isn't on your list already. *")
        }
    }    

    def changeItem() {
        var itemChangeFrom = readLine(s"\nType the name of the item you want to change, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()
           
        if (itemNamesAndTagsMap.contains(itemChangeFrom)) {
            var itemChangeTo = readLine(s"\nType what you'd like to change ${itemChangeFrom} to, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()
            if (! itemNamesAndTagsMap.contains(itemChangeTo)) {

                ItemDao.changeFromDB(itemChangeTo, itemChangeFrom)

                // itemNamesAndTagsMap -= (itemChangeFrom)
                // itemNamesAndTagsMap += (itemChangeTo -> getTag(itemChangeTo))
                println(s"\n❋❋❋ You changed $itemChangeFrom to $itemChangeTo. ❋❋❋")
                } else {
                    println(s"You can't change $itemChangeFrom to $itemChangeTo because $itemChangeTo already exists in your list.")
                }
        } else {
            println("\n❋ This item does not exist in your list. ❋")
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
             
            if(itemNamesAndTagsMap.contains(item)) {
                println(s"List already contains $item.")
                } else {
                ItemDao.addNew(item)
                println(s"Added $item")
                }
            }

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

    def getTag(itemToGetTagFor: String): String = {
        itemToGetTagFor match {
            case "STEAK" | "BEEF" | "CHICKEN" | "FISH" | "TILAPIA" | "PORK" => "Meat / Seafood"
            case "BANANAS" | "APPLES" | "CUCUMBERS" => "Fresh Produce"
            case "CINNAMON" | "SALT" | "PAPRIKA" => "Spices"
            case _ => "Misc."
        }
    }

    def exitApp() {
        println("Goodbye!")
        continueUsingList = false
    }
}
