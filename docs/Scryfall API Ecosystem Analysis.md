# **Scryfall API Ecosystem Analysis**

## **1\. What is the Scryfall API?**

The **Scryfall API** is a REST-like interface provided by [Scryfall](https://scryfall.com/), the leading card search engine and database for the card game *Magic: The Gathering (MTG)*.

Unlike the official "Gatherer" database, Scryfall is renowned among developers for its:

* **High-Resolution Imagery:** Access to high-quality scans of almost every card printed.  
* **Fuzzy Search & Syntax:** A powerful search syntax (e.g., c:u cmc=3) that the API exposes directly.  
* **Pricing Data:** Real-time pricing from TCGPlayer, Cardmarket, and CardHoarder.  
* **JSON Objects:** It returns card data in a standardized, easy-to-parse JSON format (the "Scryfall Object Model").  
* **Bulk Data:** Endpoints to download massive JSON files containing every card in the database for local caching.

It is the backbone for arguably the majority of modern third-party MTG applications, deck builders, and market trackers.

## **2\. Project Comparison Matrix**

The following table compares 20 open-source projects utilizing the Scryfall API.

| Project Name | Short Description | Primary Focus / Use Case | Language | Target Platform | API Coverage |
| :---- | :---- | :---- | :---- | :---- | :---- |
| **[scrython](https://www.google.com/search?q=https://github.com/NandaScott/scrython)** | Asynchronous wrapper for Python. | **Async Wrapper / Bots** | Python | Python Lib | **High** (Cards, Rulings, Sets) |
| [**scryfall-client**](https://www.google.com/search?q=https://github.com/scryfall/scryfall-client) | The official JavaScript client. | **Official JS Client** | JS/TS | Node.js / Browser | **Medium** (Focus on search/cards) |
| [**Scryfall-Kotlin**](https://www.google.com/search?q=https://github.com/BluefireMage/Scryfall-Kotlin) | Type-safe wrapper for Kotlin. | **Android / JVM Dev** | Kotlin | JVM / Android | **High** (Strict typing) |
| [**scryfall-rs**](https://www.google.com/search?q=https://github.com/teovc/scryfall-rs) | Rust interface for Scryfall. | **High-Perf Wrapper** | Rust | Rust Lib | **High** (Struct-based mapping) |
| [**go-scryfall**](https://github.com/BlueMonday/go-scryfall) | Go client library. | **Backend Services** | Go | Go Lib | **Full** (Comprehensive) |
| [**Scryfall.NET**](https://www.google.com/search?q=https://github.com/MtgDb/ScryfallApi.Net) | .NET Standard SDK. | **Windows / Unity Dev** | C\# | .NET / Windows | **High** |
| **[ScryfallKit](https://github.com/JacobHearst/ScryfallKit)** | Swift wrapper for iOS/macOS. | **Apple Ecosystem** | Swift | iOS / macOS | **High** |
| **[scryfall-php](https://www.google.com/search?q=https://github.com/scryfall-php/scryfall-api-client)** | PHP client for Scryfall. | **Web Backends** | PHP | Web / Server | **Medium** |
| **[scryer](https://www.google.com/search?q=https://github.com/savannabits/scryer)** | Crystal language wrapper. | **Crystal Wrapper** | Crystal | Crystal Lib | **Medium** |
| **[el-scryfall](https://www.google.com/search?q=https://github.com/wugg/el-scryfall)** | Client for Emacs. | **Editor Plugin** | Emacs Lisp | Emacs Editor | **Partial** (Search focus) |
| [**clj-scryfall**](https://www.google.com/search?q=https://github.com/wcm/clj-scryfall) | Clojure wrapper. | **Data Analysis** | Clojure | JVM | **Partial** |
| **[mpc-scryfall](https://www.google.com/search?q=https://github.com/Presgraves/mpc-scryfall)** | Proxy generation automation tool. | **Proxy Image Prep** | Python | CLI / Script | **Specific** (Images/Bulk data) |
| [**mtg-proxy-gen**](https://www.google.com/search?q=https://github.com/Not-Hub/mtg-proxy-gen) | High-res proxy generator. | **PDF Proxy Gen** | Python | CLI | **Specific** (Image fetch) |
| [**Scryfall-Discord-Bot**](https://www.google.com/search?q=https://github.com/SlightlyTooFar/Scryfall-Discord-Bot) | Bot for fetching cards in chat. | **Chat Integration** | JavaScript | Discord/Node | **Specific** (Card fetch/Price) |
| [**scryfall\_flutter**](https://www.google.com/search?q=https://github.com/joshuabaker/scryfall_flutter) | Flutter plugin for Mobile Apps. | **Cross-Platform Mobile** | Dart | Android / iOS | **Medium** |
| **[CardScanner](https://www.google.com/search?q=https://github.com/nicolas-siplis/card-scanner)** | OCR app to identify cards. | **OCR / Identification** | Python | Desktop | **Specific** (ID lookup) |
| [**ruby-scryfall**](https://www.google.com/search?q=https://github.com/braebo/ruby-scryfall) | Ruby gem wrapper. | **Ruby Wrapper** | Ruby | Ruby Gem | **Medium** |
| **[scryfall\_api](https://www.google.com/search?q=https://github.com/dr4ift/scryfall_api)** | Elixir wrapper. | **Concurrency / Elixir** | Elixir | Hex Package | **Medium** |
| **[MTG-Art-Downloader](https://www.google.com/search?q=https://github.com/ImKyle/MTG-Art-Downloader)** | Downloads art crops. | **Asset / Art Fetching** | C\# | Windows Desktop | **Specific** (Art endpoints) |
| [**cube-cobra**](https://github.com/dekkerglen/CubeCobra) | Cube management web app. | **Cube Management** | JS | Web App | **Complex** (Uses bulk data) |

## **3\. Detailed Project List**

### **Libraries & SDKs**

#### **1\. scrython**

* **Link:** [https://github.com/NandaScott/scrython](https://www.google.com/search?q=https://github.com/NandaScott/scrython)  
* **Description:** One of the most popular Python wrappers. It is designed to be asynchronous (asyncio) which is essential for bots and web apps that need to handle rate limiting gracefully. It maps Scryfall JSON objects to Python classes.  
* **Key Feature:** Auto-throttling to respect Scryfall's API rate limits.

#### **2\. scryfall-client (Official)**

* **Link:** [https://github.com/scryfall/scryfall-client](https://www.google.com/search?q=https://github.com/scryfall/scryfall-client)  
* **Description:** This is Scryfall's own JavaScript client. It is lightweight and capable of running in both the browser and Node.js environments.  
* **Key Feature:** Zero-dependency (or very low dependency) focus, making it easy to drop into lightweight frontends.

#### **3\. go-scryfall**

* **Link:** [https://github.com/BlueMonday/go-scryfall](https://github.com/BlueMonday/go-scryfall)  
* **Description:** A robust Go library. Go's static typing matches well with Scryfall's predictable JSON structure.  
* **Key Feature:** Extensive test coverage and strict adherence to the Scryfall object model.

#### **4\. Scryfall-Kotlin**

* **Link:** [https://github.com/BluefireMage/Scryfall-Kotlin](https://www.google.com/search?q=https://github.com/BluefireMage/Scryfall-Kotlin)  
* **Description:** Primarily used for Android development. It utilizes Kotlin Coroutines for asynchronous network calls.  
* **Key Feature:** Fully typed objects, reducing the risk of NullPointerExceptions when handling optional API fields (like flavor\_text).

#### **5\. scryfall-rs**

* **Link:** [https://github.com/teovc/scryfall-rs](https://www.google.com/search?q=https://github.com/teovc/scryfall-rs)  
* **Description:** A Rust wrapper that uses serde for serialization/deserialization.  
* **Key Feature:** High performance; ideal for tools that need to process Scryfall's "Bulk Data" files (1GB+ JSON files) rapidly.

#### **6\. Scryfall.NET**

* **Link:** [https://github.com/MtgDb/ScryfallApi.Net](https://www.google.com/search?q=https://github.com/MtgDb/ScryfallApi.Net)  
* **Description:** A C\# library targeting .NET Standard, making it usable in Xamarin (Mobile), Unity (Games), and generic Windows apps.  
* **Key Feature:** Integration with standard .NET HTTP clients and dependency injection patterns.

#### **7\. ScryfallKit**

* **Link:** [https://github.com/JacobHearst/ScryfallKit](https://github.com/JacobHearst/ScryfallKit)  
* **Description:** A modern Swift library designed for iOS 13+ and macOS.  
* **Key Feature:** Uses Swift's Combine framework and async/await concurrency features.

#### **8\. scryfall-php**

* **Link:** [https://github.com/scryfall-php/scryfall-api-client](https://www.google.com/search?q=https://github.com/scryfall-php/scryfall-api-client)  
* **Description:** Allows integration of Scryfall data into PHP-based websites (WordPress plugins, forums, etc.).  
* **Key Feature:** PSR-18 HTTP Client compatible.

#### **9\. ruby-scryfall**

* **Link:** [https://github.com/braebo/ruby-scryfall](https://www.google.com/search?q=https://github.com/braebo/ruby-scryfall)  
* **Description:** A Ruby gem for accessing the API.  
* **Key Feature:** Idiomatic Ruby syntax, making card fetching feel like standard ActiveRecord queries.

#### **10\. scryfall\_flutter**

* **Link:** [https://github.com/joshuabaker/scryfall\_flutter](https://www.google.com/search?q=https://github.com/joshuabaker/scryfall_flutter)  
* **Description:** A plugin for Flutter, enabling cross-platform mobile app development (iOS/Android) with a single codebase.  
* **Key Feature:** Typed models for Dart.

### **Tools, Apps, and Utilities**

#### **11\. mpc-scryfall**

* **Link:** [https://github.com/Presgraves/mpc-scryfall](https://www.google.com/search?q=https://github.com/Presgraves/mpc-scryfall)  
* **Description:** A utility script designed to format Scryfall images for "MakePlayingCards.com".  
* **Key Feature:** Automates the "bleed edge" addition to images so they print correctly as proxies.

#### **12\. Scryfall-Discord-Bot**

* **Link:** [https://github.com/SlightlyTooFar/Scryfall-Discord-Bot](https://www.google.com/search?q=https://github.com/SlightlyTooFar/Scryfall-Discord-Bot)  
* **Description:** A bot that listens for card names in brackets \[\[Black Lotus\]\] and posts the image/price.  
* **Key Feature:** Resolves fuzzy text matches using Scryfall's search algorithm to find the intended card even with typos.

#### **13\. el-scryfall**

* **Link:** [https://github.com/wugg/el-scryfall](https://www.google.com/search?q=https://github.com/wugg/el-scryfall)  
* **Description:** An Emacs package that allows you to search and view Magic cards directly inside the text editor.  
* **Key Feature:** Inserts card text or links directly into buffers; useful for writing articles or decklists in Org-mode.

#### **14\. Cube Cobra**

* **Link:** [https://github.com/dekkerglen/CubeCobra](https://github.com/dekkerglen/CubeCobra)  
* **Description:** A massive open-source web application for managing "Cubes" (custom draft sets).  
* **Key Feature:** While it has its own database, it relies heavily on Scryfall's Bulk Data and card pricing APIs to keep thousands of user lists updated.

#### **15\. MTG-Art-Downloader**

* **Link:** [https://github.com/ImKyle/MTG-Art-Downloader](https://www.google.com/search?q=https://github.com/ImKyle/MTG-Art-Downloader)  
* **Description:** A Windows Forms application focused specifically on the image\_uris part of the API.  
* **Key Feature:** Batch downloading of art crops for wallpaper or content creation purposes.

#### **16\. mtg-proxy-gen**

* **Link:** [https://github.com/Not-Hub/mtg-proxy-gen](https://www.google.com/search?q=https://github.com/Not-Hub/mtg-proxy-gen)  
* **Description:** A command-line tool that takes a text decklist and generates a printable PDF.  
* **Key Feature:** Fetches specific printings (sets) of cards using Scryfall IDs to ensure the exact version the user wants is printed.

#### **17\. clj-scryfall**

* **Link:** [https://github.com/wcm/clj-scryfall](https://www.google.com/search?q=https://github.com/wcm/clj-scryfall)  
* **Description:** A Functional Programming approach to the API using Clojure.  
* **Key Feature:** Data-centric design; treats Scryfall data as pure maps, ideal for data analysis REPLs.

#### **18\. CardScanner**

* **Link:** [https://github.com/nicolas-siplis/card-scanner](https://www.google.com/search?q=https://github.com/nicolas-siplis/card-scanner)  
* **Description:** Uses OpenCV for image recognition to identify a card from a webcam feed, then queries Scryfall for the details.  
* **Key Feature:** Bridges physical media (camera) with digital API data.

#### **19\. scryer (Crystal)**

* **Link:** [https://github.com/savannabits/scryer](https://www.google.com/search?q=https://github.com/savannabits/scryer)  
* **Description:** A wrapper for the Crystal programming language.  
* **Key Feature:** Extremely fast performance due to Crystal's compiled nature, similar to Go/Rust.

#### **20\. scryfall\_api (Elixir)**

* **Link:** [https://github.com/dr4ift/scryfall\_api](https://www.google.com/search?q=https://github.com/dr4ift/scryfall_api)  
* **Description:** An Elixir wrapper.  
* **Key Feature:** Leverages the BEAM virtual machine for high concurrency, suitable for backends serving many users simultaneously.