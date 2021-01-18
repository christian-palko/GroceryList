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
import java.util.regex.Pattern
import scala.collection.mutable.ListMap
import scala.util.matching

class GroceryListCLI extends AnsiColor{
  
    var itemNamesAndTagsMap = collection.mutable.Map[String, String]()
    var continueUsingList = false

    // TO DO: 
    // Find way to organize printed itemNamesAndTagsMaps 
    // Find way to make printed output neater
    // 

    def useList() {
        continueUsingList = true
        while (continueUsingList) {

            printList()
            val input = readLine(s"${BLINK}What would you like to do? ${RESET}\n" +
            s"\n${BLUE}${BOLD}ADD${RESET} items" +
            s"\n${BLUE}${BOLD}CHANGE${RESET} an item" +
            s"\n${BLUE}${BOLD}DELETE${RESET} an item" +
            s"\n${BLUE}${BOLD}CLEAR${RESET} to delete all items" +
            s"\n${BLUE}${BOLD}IMPORT${RESET} a list" +
            s"\n${BLUE}${BOLD}EXIT${RESET}" +
            "\n--")

            input.toUpperCase match {
                case "ADD" | "A" => addItem()
                case "CHANGE" | "C" => changeItem()     
                case "DELETE" | "D" => deleteItem()
                case "CLEAR" | "C" => clearAll()
                case "IMPORT" | "I" => appendListWithImport()
                case "EXIT" | "E" => exitApp()
                case _ => println("\n* You did not enter a valid input. *\n")     
            }
        }
    }



    def printList() {
        itemNamesAndTagsMap.clear()
        var retrievedList = ItemDao.getAll().to(ArrayBuffer)
        
        for (i <- retrievedList) {
            val itemParsed = i.item.toUpperCase
            var dept = ""
            if (i.dept_name == null) {
                dept = "Misc."
            } else {
                dept = i.dept_name
            }  
            itemNamesAndTagsMap += (itemParsed -> dept)
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
            var add = readLine(s"\nType the name of ${MAGENTA}${BOLD}one${RESET} item to add.\nOr type ${MAGENTA}${BOLD}several${RESET} item names, " +
              s"separated by commas.\nOr type ${BLUE}${BOLD}BACK${RESET} to go back:\n--").toUpperCase().trim.split(",").to(ArrayBuffer)

            for (a <- add) { 
                var trimmedItem = a.trim()
                trimmedItem match {
                case "BACK" | "B" | "EXIT" | "QUIT" | "STOP" => keepAdding = false
                case _ =>
                    ItemDao.addNew(trimmedItem)
                    printList()
                    println(s"\nAdded: $trimmedItem\n")
                }  
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
    
    def clearAll() {
        var clear = readLine(s"\n${RED}${BOLD}Are you sure you want to clear all the items from your list?${RESET}\n").toUpperCase()
        clear match {
            case "NO" | "N" | "BACK" | "CANCEL" => println("* Cancelled list clearing. *")
            case "YES" | "Y" | "CLEAR" => ItemDao.clearAllFromDB()
            case _ => println("* Cancelled list clearing. Invalid input. *")
        }
    }

    def changeItem() {
        var itemChangeFrom = readLine(s"\nType the name of the item you want to change, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()
           
        if (itemNamesAndTagsMap.contains(itemChangeFrom)) {
            var itemChangeTo = readLine(s"\nType what you'd like to change ${itemChangeFrom} to, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()
            if (! itemNamesAndTagsMap.contains(itemChangeTo)) {

                ItemDao.changeFromDB(itemChangeTo, itemChangeFrom)
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

    def exitApp() {
        println("Goodbye!")
        continueUsingList = false
    }
}
