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
    var itemNamesAndTagsMap = collection.mutable.LinkedHashMap[String, String]()
    var continueUsingList = false

    def useList() {
        continueUsingList = true
        while (continueUsingList) {

            printList()
            val input = readLine(s"What would you like to do?\n" +
            s"\n${BLUE}${BOLD}ADD${RESET} items" +
            s"\n${BLUE}${BOLD}CHANGE${RESET} an item" +
            s"\n${BLUE}${BOLD}DELETE${RESET} an item" +
            s"\n${BLUE}${BOLD}CLEAR${RESET} to delete all items" +
            s"\n${BLUE}${BOLD}IMPORT${RESET} a list" +
            s"\n${BLUE}${BOLD}EXIT${RESET}" +
            s"\n--")

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
        println(s"\n       ${GREEN}${BOLD}GROCERY LIST\n────────────────────────────\nItem Name        Department\n${RESET}")
        for ((i,t) <- itemNamesAndTagsMap) {

            println(f"$i%-15s  $t")
        }
        if (itemNamesAndTagsMap.size == 0) {
            println(s"${WHITE}         [List Empty]${RESET}")
        }
        println(s"${GREEN}${BOLD}────────────────────────────\n${RESET}")

    }

    def addItem() {
        var keepAdding = true
        while (keepAdding == true) {
            var add = readLine(s"\nType ${MAGENTA}${BOLD}one${RESET} item\nOr type ${MAGENTA}${BOLD}several${RESET} items, " +
              s"separated by commas.\nOr type ${BLUE}${BOLD}BACK${RESET} to go back:\n--").toUpperCase().trim.split(",").to(ArrayBuffer)

            for (a <- add) {
                var trimmedItem = a.trim()
                if (itemNamesAndTagsMap.contains(a.trim)) {
                    println(s"${RED}List already contains $a${RESET}")
                } else {
                    trimmedItem match {
                    case "BACK" | "B" | "EXIT" | "QUIT" | "STOP" => keepAdding = false
                    case _ =>
                        ItemDao.addNew(trimmedItem)
                        println(s"${GREEN}Added: $trimmedItem${RESET}")
                    }  
                }
            }   
            printList()
        }
    }

    def appendListWithImport () {
        var file = readLine("\nWhat is the name of the file you want to import? \n")
            
        try{
        val openFile = FileUtil.importFile(file)
            val openFileAdd = openFile
                .toUpperCase
                .trim.split(",")
                .to(ArrayBuffer)
            for (item <- openFileAdd) {
                var itemTrimmed = item.trim
                if(itemNamesAndTagsMap.contains(itemTrimmed)) {
                    println(s"${RED}List already contains ${itemTrimmed}.${RESET}")
                    } else {
                    ItemDao.addNew(itemTrimmed)
                    println(s"${GREEN}Added ${itemTrimmed}${RESET}")
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

    def deleteItem() {
        var delete = readLine(s"\nType the name of the item you want to delete, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()

        if (itemNamesAndTagsMap.contains(delete)) {
            println(delete)

            ItemDao.deleteFromDB(delete)
            println(s"\n${GREEN}❋❋❋ You've deleted ${delete} from your grocery list. ❋❋❋${RESET}")
        }
        else {
            println(s"\n${RED}* This item isn't on your list already. *${RESET}")
        }
    } 
    
    def clearAll() {
        var clear = readLine(s"\n${RED}${BOLD}Are you sure you want to clear all the items from your list?${RESET}\n").toUpperCase()
        clear match {
            case "NO" | "N" | "BACK" | "CANCEL" => println(s"${GREEN}* Cancelled list clearing. *${RESET}")
            case "YES" | "Y" | "CLEAR" => ItemDao.clearAllFromDB()
            case _ => println(s"${YELLOW}* Cancelled list clearing. Invalid input. *${RESET}")
        }
    }

    def changeItem() {
        var itemChangeFrom = readLine(s"\nType the name of the item you want to change, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()
           
        if (itemNamesAndTagsMap.contains(itemChangeFrom)) {
            var itemChangeTo = readLine(s"\nType what you'd like to change ${itemChangeFrom} to, or ${BLUE}${BOLD}BACK${RESET} to go back:\n").toUpperCase()
            if (! itemNamesAndTagsMap.contains(itemChangeTo)) {

                ItemDao.changeFromDB(itemChangeTo, itemChangeFrom)
                println(s"\n${GREEN}❋❋❋ You changed $itemChangeFrom to $itemChangeTo. ❋❋❋${RESET}")
                } else {
                    println(s"${RED}You can't change $itemChangeFrom to $itemChangeTo because $itemChangeTo already exists in your list.${RESET}")
                }
        } else {
            println(s"\n${YELLOW}❋ This item does not exist in your list. ❋${RESET}")
        }
    }



    def exitApp() {
        println(s"${GREEN}${BOLD}Goodbye!${RESET}")
        continueUsingList = false
    }
}
