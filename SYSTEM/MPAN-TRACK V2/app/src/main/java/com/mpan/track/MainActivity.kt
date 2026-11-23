package com.mpan.track

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Forest = Color(0xFF1E3F20)
private val DeepForest = Color(0xFF122814)
private val Leaf = Color(0xFF3A6B3D)
private val Sage = Color(0xFFE7EEDC)
private val Cream = Color(0xFFF7F7F0)
private val Paper = Color(0xFFFFFEFA)
private val Ink = Color(0xFF203322)
private val Muted = Color(0xFF758071)
private val Line = Color(0xFFE2E6DA)

data class VendorInfo(
    val name: String,
    val location: String,
    val schedule: String,
    val price: String = "₱25.00"
)

private val vendorList = listOf(
    VendorInfo("R1CZ CART", "Nasugbu Public Market, Nasugbu, Batangas", "Mon–Sat • 5:00 AM – 9:00 AM"),
    VendorInfo("HANZ CART", "Barangay 1, Nasugbu, Batangas", "Mon–Sat • 5:30 AM – 9:30 AM"),
    VendorInfo("SHANES CART", "Barangay 4, Nasugbu, Batangas", "Daily • 5:00 AM – 10:00 AM"),
    VendorInfo("VIOLA CART", "Wawa, Nasugbu, Batangas", "Mon–Sun • 6:00 AM – 9:00 AM"),
    VendorInfo("AZ CART", "Bucana, Nasugbu, Batangas", "Daily • 5:00 AM – 8:30 AM"),
    VendorInfo("THEO CART", "Kaylaway, Nasugbu, Batangas", "Mon–Sat • 5:00 AM – 9:00 AM"),
    VendorInfo("JOYCE CART", "Talangan, Nasugbu, Batangas", "Daily • 5:30 AM – 9:30 AM")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MpanTheme {
                MpanTrackApp()
            }
        }
    }
}

@Composable
private fun MpanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Forest,
            onPrimary = Color.White,
            secondary = Leaf,
            background = Cream,
            surface = Paper,
            onSurface = Ink
        ),
        typography = Typography(),
        content = content
    )
}

private enum class Page {
    LOGIN, REGISTER, CUSTOMER_HOME, MAP, VENDOR_DETAILS, PROFILE,
    VENDOR_HOME, MY_PRODUCTS, MY_SCHEDULE, ADMIN_HOME, MANAGE_VENDORS, RECORDS
}

@Composable
private fun MpanTrackApp() {
    var page by remember { mutableStateOf(Page.LOGIN) }
    var role by remember { mutableStateOf("Customer") }
    var search by remember { mutableStateOf("") }
    var selectedVendor by remember { mutableStateOf(vendorList[0]) }
    var productName by remember { mutableStateOf("Malunggay Pandesal") }
    var description by remember { mutableStateOf("Freshly baked malunggay pandesal.") }
    var price by remember { mutableStateOf("25.00") }
    var available by remember { mutableStateOf(true) }
    var scheduleStart by remember { mutableStateOf("5:00 AM") }
    var scheduleEnd by remember { mutableStateOf("9:00 AM") }
    var selectedDay by remember { mutableStateOf("M") }
    var showSaved by remember { mutableStateOf(false) }

    val hasBottomBar = page in listOf(
        Page.CUSTOMER_HOME, Page.MAP, Page.PROFILE,
        Page.VENDOR_HOME, Page.MY_PRODUCTS, Page.MY_SCHEDULE,
        Page.ADMIN_HOME, Page.MANAGE_VENDORS, Page.RECORDS
    )

    Scaffold(
        containerColor = Cream,
        bottomBar = {
            if (hasBottomBar) {
                MpanBottomBar(page = page, role = role, onNavigate = { page = it })
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (page) {
                Page.LOGIN -> LoginScreen(
                    role = role,
                    onRoleChange = { role = it },
                    onLogin = {
                        page = when (role) {
                            "Vendor" -> Page.VENDOR_HOME
                            "Admin" -> Page.ADMIN_HOME
                            else -> Page.CUSTOMER_HOME
                        }
                    },
                    onRegister = { page = Page.REGISTER }
                )
                Page.REGISTER -> RegisterScreen(
                    onBack = { page = Page.LOGIN },
                    onCreate = { page = Page.LOGIN }
                )
                Page.CUSTOMER_HOME -> CustomerHomeScreen(
                    search = search,
                    onSearch = { search = it },
                    onMap = { page = Page.MAP },
                    onVendorSelect = { v ->
                        selectedVendor = v
                        page = Page.VENDOR_DETAILS
                    }
                )
                Page.MAP -> MapScreen(
                    selectedVendor = selectedVendor,
                    onVendorSelect = { v ->
                        selectedVendor = v
                        page = Page.VENDOR_DETAILS
                    }
                )
                Page.VENDOR_DETAILS -> VendorDetailsScreen(
                    vendor = selectedVendor,
                    onBack = { page = if (role == "Vendor") Page.VENDOR_HOME else Page.CUSTOMER_HOME }
                )
                Page.PROFILE -> ProfileScreen(onLogout = { page = Page.LOGIN })
                Page.VENDOR_HOME -> VendorHomeScreen(
                    vendor = selectedVendor,
                    onProducts = { page = Page.MY_PRODUCTS },
                    onSchedule = { page = Page.MY_SCHEDULE },
                    onProfile = { page = Page.PROFILE },
                    onMap = { page = Page.MAP }
                )
                Page.MY_PRODUCTS -> MyProductsScreen(
                    name = productName, onName = { productName = it },
                    description = description, onDescription = { description = it },
                    price = price, onPrice = { price = it },
                    available = available, onAvailable = { available = it },
                    saved = showSaved, onSave = { showSaved = true }
                )
                Page.MY_SCHEDULE -> ScheduleScreen(
                    start = scheduleStart, onStart = { scheduleStart = it },
                    end = scheduleEnd, onEnd = { scheduleEnd = it },
                    selectedDay = selectedDay, onDay = { selectedDay = it },
                    onSave = { showSaved = true }
                )
                Page.ADMIN_HOME -> AdminHomeScreen(
                    onVendors = { page = Page.MANAGE_VENDORS },
                    onRecords = { page = Page.RECORDS },
                    onMap = { page = Page.MAP }
                )
                Page.MANAGE_VENDORS -> ManageVendorsScreen(
                    onVendorSelect = { v ->
                        selectedVendor = v
                        page = Page.VENDOR_DETAILS
                    }
                )
                Page.RECORDS -> RecordsScreen()
            }
            if (showSaved && page in listOf(Page.MY_PRODUCTS, Page.MY_SCHEDULE)) {
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                    action = { TextButton(onClick = { showSaved = false }) { Text("OK") } }
                ) { Text("Changes saved in this demo.") }
            }
        }
    }
}

@Composable
private fun MpanBottomBar(page: Page, role: String, onNavigate: (Page) -> Unit) {
    val items = when (role) {
        "Admin" -> listOf(Triple("Dashboard", "⌂", Page.ADMIN_HOME), Triple("Carts", "▦", Page.MANAGE_VENDORS), Triple("Records", "≡", Page.RECORDS))
        "Vendor" -> listOf(Triple("Home", "⌂", Page.VENDOR_HOME), Triple("Products", "▣", Page.MY_PRODUCTS), Triple("Schedule", "◷", Page.MY_SCHEDULE), Triple("Profile", "○", Page.PROFILE))
        else -> listOf(Triple("Home", "⌂", Page.CUSTOMER_HOME), Triple("Map", "⌖", Page.MAP), Triple("Profile", "○", Page.PROFILE))
    }
    NavigationBar(containerColor = Paper, contentColor = Forest) {
        items.forEach { item ->
            NavigationBarItem(
                selected = page == item.third,
                onClick = { onNavigate(item.third) },
                icon = { Text(item.second, fontSize = 21.sp, fontWeight = FontWeight.Bold) },
                label = { Text(item.first, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Forest,
                    selectedTextColor = Forest,
                    indicatorColor = Sage,
                    unselectedIconColor = Muted,
                    unselectedTextColor = Muted
                )
            )
        }
    }
}

@Composable
private fun BrandHeader(subtitle: String = "A Malunggay Pandesal Tracker") {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.size(66.dp).background(Sage, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("MP", color = Forest, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        Text("mpan / track", color = Forest, fontSize = 27.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text(subtitle, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun PageTitle(title: String, subtitle: String? = null, onBack: (() -> Unit)? = null) {
    Column(Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        if (onBack != null) {
            Text("‹  Back", color = Forest, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onBack() }.padding(bottom = 14.dp))
        }
        Text(title, color = Forest, fontSize = 29.sp, lineHeight = 34.sp,
            fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        if (subtitle != null) {
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = Muted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun GreenButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = Color.White)
    ) { Text(text, fontWeight = FontWeight.SemiBold) }
}

@Composable
private fun SoftCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Paper),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Line)
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun Field(label: String, value: String, onValue: (String) -> Unit, placeholder: String = "") {
    Column {
        Text(label, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValue,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Muted, fontSize = 13.sp) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Leaf,
                unfocusedBorderColor = Line,
                focusedContainerColor = Paper,
                unfocusedContainerColor = Paper
            )
        )
    }
}

@Composable
private fun LoginScreen(role: String, onRoleChange: (String) -> Unit, onLogin: () -> Unit, onRegister: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Cream).padding(horizontal = 26.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BrandHeader("Fresh bread in Nasugbu, Batangas.")
        Spacer(Modifier.height(30.dp))
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Field("Email address", email, { email = it }, "you@example.com")
        Spacer(Modifier.height(13.dp))
        Field("Password", password, { password = it }, "Enter your password")
        Spacer(Modifier.height(14.dp))
        Text("Sign in as", color = Muted, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            listOf("Customer", "Vendor", "Admin").forEach {
                FilterChip(selected = role == it, onClick = { onRoleChange(it) }, label = { Text(it) })
            }
        }
        Spacer(Modifier.height(18.dp))
        GreenButton("Log in  →", onLogin)
        TextButton(onClick = { }) { Text("Forgot password?", color = Forest, fontSize = 12.sp) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account? ", color = Muted, fontSize = 12.sp)
            TextButton(onClick = onRegister, contentPadding = PaddingValues(0.dp)) {
                Text("Create account", color = Forest, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
        Text("FRESHLY MADE • NASUGBU, BATANGAS", color = Muted, fontSize = 9.sp, letterSpacing = 2.sp)
    }
}

@Composable
private fun RegisterScreen(onBack: () -> Unit, onCreate: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(Cream).padding(24.dp)) {
        PageTitle("A fresher start.", "Create your customer account", onBack)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Field("Full name", name, { name = it }, "Your full name") }
            item { Field("Username", username, { username = it }, "Choose a username") }
            item { Field("Email address", email, { email = it }, "you@example.com") }
            item { Field("Password", password, { password = it }, "Create a password") }
            item { Field("Confirm password", confirm, { confirm = it }, "Repeat your password") }
            item {
                Spacer(Modifier.height(8.dp))
                GreenButton("Create account", onCreate)
                TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Already have an account? Log in", color = Forest) }
            }
        }
    }
}

@Composable
private fun CustomerHomeScreen(
    search: String,
    onSearch: (String) -> Unit,
    onMap: () -> Unit,
    onVendorSelect: (VendorInfo) -> Unit
) {
    val filteredVendors = vendorList.filter {
        it.name.contains(search, true) || it.location.contains(search, true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Cream).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("mpan / track", color = Forest, fontWeight = FontWeight.Bold, fontSize = 19.sp)
            Spacer(Modifier.height(24.dp))
            Text("Nasugbu Fresh Carts.", color = Forest, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text("Malunggay Pandesal (₱25.00) across Nasugbu, Batangas.", color = Muted, fontSize = 13.sp)
        }
        item {
            OutlinedTextField(
                value = search, onValueChange = onSearch, modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("⌕  Search cart (e.g. R1CZ, Hanz, Wawa)") },
                shape = RoundedCornerShape(14.dp), singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Line, focusedBorderColor = Leaf, unfocusedContainerColor = Paper)
            )
        }
        item {
            SoftCard {
                Text("TODAY'S SPECIAL", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Spacer(Modifier.height(8.dp))
                Text("Malunggay Pandesal — ₱25.00", color = Forest, fontSize = 21.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                Text("Freshly baked hot pandesal available across 7 mobile carts in Nasugbu, Batangas.", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
                Text("View Nasugbu map  →", color = Forest, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onMap() })
            }
        }
        item { SectionHeading("Active Carts in Nasugbu", "${filteredVendors.size} carts") }
        items(filteredVendors) { vendor ->
            VendorCard(vendor = vendor, onClick = { onVendorSelect(vendor) })
        }
        item { GreenButton("⌖  Open Nasugbu selling-location map", onMap) }
    }
}

@Composable
private fun VendorCard(vendor: VendorInfo, onClick: () -> Unit) {
    SoftCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(58.dp).background(Sage, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Text("🥖", fontSize = 27.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(vendor.name, color = Ink, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(vendor.location, color = Muted, fontSize = 11.sp)
                Spacer(Modifier.height(2.dp))
                Text("Malunggay Pandesal • ${vendor.price} • Available", color = Leaf, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            Text("›", color = Forest, fontSize = 25.sp, modifier = Modifier.clickable { onClick() })
        }
        Spacer(Modifier.height(8.dp))
        Text("View exact location & schedule  →", color = Forest, fontSize = 11.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onClick() })
    }
}

@Composable
private fun SectionHeading(title: String, action: String? = null) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Forest, fontSize = 19.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, modifier = Modifier.weight(1f))
        if (action != null) Text(action, color = Leaf, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MapScreen(selectedVendor: VendorInfo, onVendorSelect: (VendorInfo) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val displayedVendors = vendorList.filter {
        it.name.contains(searchQuery, true) || it.location.contains(searchQuery, true)
    }

    Column(Modifier.fillMaxSize().background(Cream).padding(20.dp)) {
        PageTitle("Nasugbu Map.", "Explore exact selling locations in Nasugbu, Batangas")
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("⌕  Search cart or Nasugbu location") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )
        Spacer(Modifier.height(14.dp))

        // Visual map representation of Nasugbu, Batangas with all 7 carts visible
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFE5EBD9), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MapMarkerPin(vendorList[0], selectedVendor == vendorList[0], onVendorSelect)
                    MapMarkerPin(vendorList[1], selectedVendor == vendorList[1], onVendorSelect)
                    MapMarkerPin(vendorList[2], selectedVendor == vendorList[2], onVendorSelect)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    MapMarkerPin(vendorList[3], selectedVendor == vendorList[3], onVendorSelect)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MapMarkerPin(vendorList[4], selectedVendor == vendorList[4], onVendorSelect)
                    MapMarkerPin(vendorList[5], selectedVendor == vendorList[5], onVendorSelect)
                    MapMarkerPin(vendorList[6], selectedVendor == vendorList[6], onVendorSelect)
                }
            }
            Text(
                "Nasugbu, Batangas — Tap any cart pin for exact location",
                color = Forest,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(Paper, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Spacer(Modifier.height(14.dp))
        SoftCard {
            Text("SELECTED CART LOCATION", color = Leaf, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.height(4.dp))
            Text(selectedVendor.name, color = Forest, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text("📍 ${selectedVendor.location}", color = Ink, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text("Malunggay Pandesal • ${selectedVendor.price}", color = Leaf, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("🕒 ${selectedVendor.schedule}", color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(10.dp))
            GreenButton("View cart details", onClick = { onVendorSelect(selectedVendor) })
        }
    }
}

@Composable
private fun MapMarkerPin(vendor: VendorInfo, isSelected: Boolean, onSelect: (VendorInfo) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSelect(vendor) }
            .background(if (isSelected) Forest else Sage, RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Text("📍", fontSize = 20.sp)
        Text(vendor.name, color = if (isSelected) Color.White else Forest, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun VendorDetailsScreen(vendor: VendorInfo, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Cream).padding(20.dp)) {
        PageTitle("Cart profile.", "Exact location and pricing in Nasugbu", onBack)
        SoftCard {
            Box(Modifier.fillMaxWidth().height(140.dp).background(Sage, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🥖", fontSize = 32.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(vendor.name, color = Forest, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(vendor.name, color = Forest, fontSize = 23.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text("📍 Exact Location: ${vendor.location}", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text("Nasugbu, Batangas", color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(14.dp))
            Text("AVAILABLE PRODUCT", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            SoftCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Malunggay Pandesal", color = Ink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Freshly baked hot pandesal", color = Muted, fontSize = 11.sp)
                        Text("Price: ${vendor.price} / piece", color = Leaf, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("Available", color = Leaf, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Selling hours", color = Forest, fontWeight = FontWeight.Bold)
            Text(vendor.schedule, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ProfileScreen(onLogout: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Cream).padding(20.dp)) {
        PageTitle("Your account.", "Manage your MPAN-Track profile")
        SoftCard {
            Box(Modifier.size(64.dp).background(Sage, CircleShape), contentAlignment = Alignment.Center) {
                Text("MC", color = Forest, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text("Nasugbu Customer", color = Forest, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text("Nasugbu, Batangas", color = Muted, fontSize = 12.sp)
        }
        Spacer(Modifier.height(16.dp))
        listOf("Personal information", "Nasugbu location settings", "Change password", "Help and support").forEach {
            SoftCard { Row(verticalAlignment = Alignment.CenterVertically) {
                Text(it, color = Ink, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                Text("›", color = Forest, fontSize = 22.sp)
            } }
            Spacer(Modifier.height(9.dp))
        }
        Spacer(Modifier.weight(1f))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(13.dp)) {
            Text("Log out", color = Forest)
        }
    }
}

@Composable
private fun VendorHomeScreen(
    vendor: VendorInfo,
    onProducts: () -> Unit,
    onSchedule: () -> Unit,
    onProfile: () -> Unit,
    onMap: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().background(Cream).padding(20.dp), contentPadding = PaddingValues(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("VENDOR SPACE", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text(vendor.name, color = Forest, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text("Nasugbu Mobile Cart • Malunggay Pandesal (₱25.00)", color = Muted, fontSize = 12.sp)
        }
        item {
            SoftCard {
                Text("CART STATUS", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(vendor.name, color = Forest, fontSize = 21.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                Text("Location: ${vendor.location}  •  Active", color = Muted, fontSize = 12.sp)
            }
        }
        item { SectionHeading("Manage your cart") }
        item { ActionRow("My products", "Malunggay Pandesal — ₱25.00", "▣", onProducts) }
        item { ActionRow("My schedule", "Set your Nasugbu selling hours", "◷", onSchedule) }
        item { ActionRow("My cart info", "Edit your cart details", "⌂", onProfile) }
        item { ActionRow("Nasugbu map", "Preview your location pin", "⌖", onMap) }
    }
}

@Composable
private fun ActionRow(title: String, subtitle: String, icon: String, onClick: () -> Unit) {
    SoftCard {
        Row(Modifier.fillMaxWidth().clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(44.dp).background(Sage, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Text(icon, color = Forest, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Forest, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, color = Muted, fontSize = 11.sp)
            }
            Text("›", color = Forest, fontSize = 25.sp)
        }
    }
}

@Composable
private fun MyProductsScreen(
    name: String, onName: (String) -> Unit,
    description: String, onDescription: (String) -> Unit,
    price: String, onPrice: (String) -> Unit,
    available: Boolean, onAvailable: (Boolean) -> Unit,
    saved: Boolean, onSave: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().background(Cream).padding(20.dp), contentPadding = PaddingValues(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { PageTitle("Cart products.", "Malunggay Pandesal priced at ₱25.00") }
        item {
            SoftCard {
                Text("EDIT PRODUCT", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(Modifier.height(12.dp))
                Field("Product name", name, onName)
                Spacer(Modifier.height(12.dp))
                Field("Description", description, onDescription)
                Spacer(Modifier.height(12.dp))
                Field("Price (PHP)", price, onPrice)
                Row(Modifier.fillMaxWidth().padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Available to customers", color = Forest, fontWeight = FontWeight.SemiBold)
                        Text(if (available) "Currently available" else "Currently unavailable", color = Muted, fontSize = 11.sp)
                    }
                    Switch(checked = available, onCheckedChange = onAvailable)
                }
                Spacer(Modifier.height(12.dp))
                GreenButton("Save product", onSave)
                TextButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Cancel", color = Muted) }
            }
        }
        item { SectionHeading("My products") }
        item {
            SoftCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(name, color = Ink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(description, color = Muted, fontSize = 11.sp)
                        Text("₱$price / piece • Available", color = Leaf, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleScreen(start: String, onStart: (String) -> Unit, end: String, onEnd: (String) -> Unit, selectedDay: String, onDay: (String) -> Unit, onSave: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Cream).padding(20.dp)) {
        PageTitle("Selling schedule.", "Set your selling days and hours in Nasugbu")
        SoftCard {
            Text("SELLING DAYS", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("M", "T", "W", "T", "F", "S", "S").forEachIndexed { index, day ->
                    val id = "$day$index"
                    FilterChip(selected = selectedDay == id, onClick = { onDay(id) }, label = { Text(day) })
                }
            }
            Spacer(Modifier.height(18.dp))
            Field("Opening time", start, onStart, "5:00 AM")
            Spacer(Modifier.height(12.dp))
            Field("Closing time", end, onEnd, "9:00 AM")
            Spacer(Modifier.height(18.dp))
            Text("LOCATION", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("Nasugbu, Batangas", color = Forest, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(18.dp))
            GreenButton("Save schedule", onSave)
        }
    }
}

@Composable
private fun AdminHomeScreen(onVendors: () -> Unit, onRecords: () -> Unit, onMap: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().background(Cream).padding(20.dp), contentPadding = PaddingValues(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("ADMIN WORKSPACE", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("Nasugbu Admin", color = Forest, fontSize = 29.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text("Managing 7 carts across Nasugbu, Batangas.", color = Muted, fontSize = 12.sp)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("7", "Carts", Modifier.weight(1f))
                StatCard("7", "Locations", Modifier.weight(1f))
                StatCard("₱25", "Price", Modifier.weight(1f))
            }
        }
        item { SectionHeading("Nasugbu MPAN-Track") }
        item { AdminGrid(onVendors, onRecords) }
        item { GreenButton("⌖  Open Nasugbu map", onMap) }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Forest)) {
        Column(Modifier.padding(13.dp)) {
            Text(value, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color(0xFFDDE8D6), fontSize = 10.sp)
        }
    }
}

@Composable
private fun AdminGrid(onVendors: () -> Unit, onRecords: () -> Unit) {
    val options = listOf(
        "Carts" to "Manage 7 mobile carts", "Locations" to "Nasugbu spots",
        "Products" to "Malunggay Pandesal", "Schedules" to "Selling hours",
        "Records" to "Activity history"
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        options.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                pair.forEach { option ->
                    SoftCard(modifier = Modifier.weight(1f)) {
                        Column(Modifier.fillMaxWidth().clickable {
                            if (option.first == "Records") onRecords() else onVendors()
                        }) {
                            Text("✦", color = Leaf, fontSize = 21.sp)
                            Text(option.first, color = Forest, fontWeight = FontWeight.Bold)
                            Text(option.second, color = Muted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManageVendorsScreen(onVendorSelect: (VendorInfo) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = vendorList.filter { it.name.contains(query, true) || it.location.contains(query, true) }

    Column(Modifier.fillMaxSize().background(Cream).padding(20.dp)) {
        PageTitle("Nasugbu Carts.", "Manage registered mobile carts")
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("⌕  Search cart or location") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )
        Spacer(Modifier.height(14.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
            items(filtered) { vendor ->
                VendorCard(vendor = vendor, onClick = { onVendorSelect(vendor) })
            }
        }
    }
}

@Composable
private fun RecordsScreen() {
    Column(Modifier.fillMaxSize().background(Cream).padding(20.dp)) {
        PageTitle("Nasugbu Updates.", "Recent activity across Nasugbu carts")
        SoftCard {
            Text("NASUGBU OVERVIEW", color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("7", color = Forest, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                Text("7", color = Forest, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                Text("₱25", color = Forest, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Active Carts", color = Muted, fontSize = 10.sp)
                Text("Nasugbu Spots", color = Muted, fontSize = 10.sp)
                Text("Pandesal Price", color = Muted, fontSize = 10.sp)
            }
        }
        Spacer(Modifier.height(14.dp))
        listOf(
            "Price updated" to "Malunggay Pandesal set to ₱25.00 across all 7 carts",
            "Location verified" to "All 7 carts positioned in Nasugbu, Batangas",
            "Schedule active" to "Daily morning selling hours operational",
            "Carts ready" to "R1CZ, Hanz, Shanes, Viola, AZ, Theo, Joyce carts online"
        ).forEach { entry ->
            SoftCard {
                Text(entry.first, color = Forest, fontWeight = FontWeight.Bold)
                Text(entry.second, color = Muted, fontSize = 12.sp)
                Text("Today  •  Nasugbu Batangas record", color = Leaf, fontSize = 10.sp)
            }
            Spacer(Modifier.height(9.dp))
        }
    }
}
