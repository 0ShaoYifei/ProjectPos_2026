from pathlib import Path
root=Path('deliverables/report/report_assets')
common='''@startuml
skinparam backgroundColor white
skinparam defaultFontName Arial
skinparam defaultFontSize 16
skinparam shadowing false
skinparam ArrowColor #333333
skinparam classBackgroundColor #F5F8F8
skinparam classBorderColor #354F52
skinparam packageBackgroundColor #F5F8F8
skinparam packageBorderColor #354F52
skinparam sequenceParticipantBackgroundColor #F5F8F8
skinparam sequenceParticipantBorderColor #354F52
skinparam dpi 160
'''
diagrams={
'01-use-cases':r'''left to right direction
actor Cashier
rectangle "ProjectPos_2026" {
 usecase "Process Sale" as Sale
 usecase "Handle Returns" as Returns
 usecase "Manage Products\nand Inventory" as Products
 usecase "Review Transactions\nand Receipts" as Review
}
Cashier -- Sale
Cashier -- Returns
Cashier -- Products
Cashier -- Review
note bottom of Returns
Customer provides the receipt and goods.
Cashier interacts with the application.
end note
''',
'02-ssd-process-sale':r'''actor Cashier
participant ":POS System" as POS
Cashier -> POS: startNewSale()
POS --> Cashier: available products and empty cart
loop Each purchased product
 Cashier -> POS: addItem(productId, quantity)
 POS --> Cashier: line details and running total
end
Cashier -> POS: makeCashPayment(cash)
alt Valid stock and sufficient payment
 POS --> Cashier: receipt, total and change
else Insufficient stock or payment
 POS --> Cashier: English error; cart retained
end
note over POS
Inventory and transaction records change
only after successful checkout.
end note
''',
'03-ssd-handle-returns':r'''actor Cashier
participant ":POS System" as POS
Cashier -> POS: findReceipt(saleId)
POS --> Cashier: purchased lines and returnable quantities
Cashier -> POS: returnItem(itemId, quantity, reason)
alt Quantity is within remaining purchase
 POS --> Cashier: cash refund and updated receipt
else Invalid or excessive quantity
 POS --> Cashier: English error; no refund recorded
end
note over POS
One returned product line per request.
Repeat for another product on the receipt.
end note
''',
'04-domain-model':r'''hide methods
hide circle
class Product {
 barcode
 name
 currentUnitPrice
 stockQuantity
 availableForSale
}
class Sale {
 saleNumber
 dateTime
 total
 cashTendered
 change
}
class SaleItem {
 purchasedQuantity
 originalUnitPrice
 productNameAtSale
 returnedQuantity
}
class ReturnRecord {
 returnedQuantity
 refundAmount
 reason
 dateTime
}
Sale "1" *-- "1..*" SaleItem : contains
Product "1" -- "0..*" SaleItem : identifies product for
SaleItem "1" -- "0..*" ReturnRecord : supports
note bottom of Sale
Sale records cash tendered
and change returned.
end note
''',
'05-package-model':r'''top to bottom direction
package "Presentation" {
 package "Thymeleaf templates" as Views
 package "controller" as Controllers
}
package "Application logic" {
 package "service" as Services
}
package "Persistence access" {
 package "repository" as Repositories
}
package "Domain data and rules" {
 package "entity" as Entities
}
package "Shared errors" {
 package "exception" as Errors
}
Views ..> Controllers : submits HTTP requests
Controllers ..> Services : delegates use cases
Services ..> Repositories : queries and locks
Services ..> Entities : applies domain rules
Repositories ..> Entities : persists entities
Controllers ..> Entities : renders read models
Controllers ..> Errors
Services ..> Errors
Entities ..> Errors
note bottom of Repositories
Spring Data JPA / Hibernate
persist entities in MySQL.
end note
''',
'06-interaction-checkout':r'''participant "web :\nSaleController" as C
participant "sales :\nSaleService" as S
participant "products" as P
participant "records :\nSaleRepository" as R
C -> S: checkout(items, cash, key)
activate S
S -> R: findByCheckoutKey(key)
R --> S: existing sale or empty
loop Product IDs in sorted order
 S -> P: findLockedById(id)
 P --> S: locked Product
end
S -> R: findByCheckoutKey(key)
R --> S: duplicate check
note over S
Validate quantities, active state,
stock and payment. Create SaleItems,
record cash and reduce stock.
end note
S -> R: saveAndFlush(sale)
R --> S: saved Sale
S --> C: saved Sale
note over S,R
One transaction commits all changes.
Any business failure rolls back.
end note
deactivate S
''',
'07-interaction-return':r'''participant "web" as C
participant "returns" as S
participant "items" as I
participant "products" as P
participant "records" as R
C -> S: process(...)
activate S
S -> R: findByRequestKey(key)
R --> S: existing refund or empty
S -> I: findLocked(itemId, saleId)
I --> S: purchased SaleItem
S -> R: findByRequestKey(key)
note over S
Check remaining quantity.
end note
S -> P: findLockedById(productId)
P --> S: Product
note over S
Restore stock and recordReturn(qty).
Refund uses original unit price.
end note
S -> R: saveAndFlush(returnRecord)
R --> S: saved refund record
S --> C: ReturnRecord
note over S,R
One transaction; rollback on failure.
end note
deactivate S
''',
'08-design-entities':r'''hide circle
skinparam classAttributeIconSize 0
class Product {
 -id : Long
 -version : Long
 -barcode : String
 -name : String
 -price : BigDecimal
 -stock : int
 -active : boolean
 +update(...)
 +decreaseStock(qty)
 +restoreStock(qty)
}
class Sale {
 -id : Long
 -checkoutKey : String
 -createdAt : LocalDateTime
 -total : BigDecimal
 -cashPaid : BigDecimal
 -changeDue : BigDecimal
 +addItem(product, qty)
 +recordPayment(cash)
 +getRefundedTotal()
}
class SaleItem {
 -id : Long
 -productName : String
 -barcode : String
 -unitPrice : BigDecimal
 -quantity : int
 -returnedQuantity : int
 +getSubtotal()
 +getReturnableQuantity()
 +recordReturn(qty)
}
class ReturnRecord {
 -id : Long
 -requestKey : String
 -quantity : int
 -refund : BigDecimal
 -reason : String
 -createdAt : LocalDateTime
}
Sale "1" *-- "1..*" SaleItem
SaleItem "0..*" --> "1" Product
ReturnRecord "0..*" --> "1" SaleItem
''',
'09-design-services':r'''hide circle
skinparam classAttributeIconSize 0
class SaleController {
 +newSale(session, model)
 +checkout(cash, key, session, redirect)
 +receipt(id, model)
}
class SaleService {
 +preview(quantities)
 +checkout(quantities, cash, key)
 +get(id)
 +all()
}
interface ProductRepository {
 +findLockedById(id)
}
interface SaleRepository {
 +findByCheckoutKey(key)
 +findDetailedById(id)
 +saveAndFlush(sale)
}
SaleController --> SaleService : delegates
SaleService --> ProductRepository : locks inventory
SaleService --> SaleRepository : records sale
note bottom of SaleRepository
Repositories extend JpaRepository.
Selected Process Sale dependencies.
end note
'''
}
for name,source in diagrams.items():
 (root/(name+'.puml')).write_text(common+source+'\n@enduml\n',encoding='utf-8')
print('Saved',len(diagrams),'diagram sources')

