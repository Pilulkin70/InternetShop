insert into products (id, vendor_code, brand_id, title, description, price, available, img_url, category_id) values
(1, 'TUF Gaming Z690', 1, 'Motherboard 1', 'Asus TUF Gaming Z690-Plus WI-FI D4 s-1700 Z690', 350.0, 'true', '/images/products/AsusZ690.jpg', 1),
(2, 'BX8070110400F', 2, 'CPU 1', 'Intel Core i5-10400F s-1200 2.9GHz/12MB BOX', 125.0, 'true', '/images/products/I510400F.jpg', 2),
(3, 'IR-X3200D464L16SA/16GDC', 3, 'RAM 1', 'GoodRam DDR4 16GB 2x8GB 3200MHz IRDM X Black', 65.0, 'true', '/images/products/GoodRAM2x8.jpg', 3),
(4, 'H500-KGNN-S00', 4, 'Case 1', 'Cooler Master MasterCase H500 Black б/БП', 115.0, 'true', '/images/products/CMV38.jpg', 4),
(5, 'WD40PURZ', 5, 'HDD 1', '3.5" SATA 4TB WD Purple', 98.0, 'true', '/images/products/WD.jpg', 5),
(6, '32MP60G-B', 6, 'Monitor 1', '32" LG 32MP60G-B', 159.0, 'true', '/images/products/LG32MP60.jpg', 6),
(7, 'KB-UMGL-01-UA', 7, 'Keyboard 1', 'Gembird KB-UMGL-01-UA Black USB', 19.0, 'true', '/images/products/GembirdKB.jpg', 7),
(8, 'X89', 8, 'Mouse 1', 'A4Tech X89 Oscar Neon Black USB', 10.0, 'true', '/images/products/A4Oscar.jpg', 8),
(9, 'RX 6500 XT MECH 2X 4G OC', 9, 'Video 1', 'MSI PCI-E Radeon RX 6500 XT 4GB DDR6', 250.0, 'true', '/images/products/RadeonRX.jpg', 9),
(10, 'ZM550-GVII', 12, 'Power Supply 1', '550W Zalman', 65.0, 'true', '/images/products/Zalman550.jpg', 10),
(11, 'B550-A Pro', 9, 'Motherboard 2', 'MSI B550-A Pro s-AM4 B550', 150.0, 'true', '/images/products/MSIB550.jpg', 1),
(12, '100-100000147BOX', 10, 'CPU 2', 'AMD Ryzen 5 4600G s-AM4 3.7GHz/8MB BOX', 125.0, 'true', '/images/products/AMDRyzen5.jpg', 2),
(13, 'KF426C16BBK2/16', 11, 'RAM 2', 'Kingston Fury DDR4 16GB 2x8GB 2666MHz Beast Black', 60.0, 'true', '/images/products/KingstonFury2x8.jpg', 3),
(14, 'Z1', 12, 'Case 2', 'Zalman Z1 NEO б/БП Black', 55.0, 'true', '/images/products/ZalmanZ1.jpg', 4),
(15, 'ST4000DM004', 13, 'HDD 2', '3.5" SATA 4TB Seagate Barracuda', 90.0, 'true', '/images/products/Barracuda4.jpg', 5),
(16, 'VG27VQ', 1, 'Monitor 2', '27" Asus VG27VQ', 230.0, 'true', '/images/products/AsusVG27VQ.jpg', 6),
(17, '90MP01X0-BKRA00', 1, 'Keyboard 2', 'Asus TUF Gaming K1 Black RU', 60.0, 'true', '/images/products/AsusK1.jpg', 7),
(18, '31040002400', 14, 'Mouse 2', 'Genius Scorpion Spear RS2', 15.0, 'true', '/images/products/GeniusRS2.jpg', 8),
(19, 'GV-N3060GAMING OC-12GD rev.2.0', 15, 'Video 2', 'GigaByte PCI-E GeForce RTX3060 LHR 12GB DDR6', 500.0, 'true', '/images/products/RTX3060.jpg', 9),
(20, 'PPS-850FC', 16, 'Power Supply 2', '850W Chieftec PPS-850FC', 130.0, 'true', '/images/products/Chieftec850.jpg', 10);

alter sequence product_seq RESTART with 21;