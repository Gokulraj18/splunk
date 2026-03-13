#!/bin/bash

# Splunk Integration Verification Script
# This script helps verify your Splunk logging setup

echo "========================================"
echo "Splunk Integration Verification"
echo "========================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check 1: Splunk HEC Connectivity
echo "Check 1: Splunk HEC Connectivity"
echo "=================================="
if curl -s -k https://localhost:8088/services/collector/health > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Splunk HEC is reachable${NC}"
    response=$(curl -s -k https://localhost:8088/services/collector/health)
    echo "  Response: $response"
else
    echo -e "${RED}✗ Splunk HEC is NOT reachable${NC}"
    echo "  Make sure Splunk is running on https://localhost:8088"
fi
echo ""

# Check 2: Application Build
echo "Check 2: Application Build Status"
echo "==================================="
cd /Users/gokulraj/Downloads/splunk
if mvn clean compile -q 2>&1 | grep -q ERROR; then
    echo -e "${RED}✗ Application has compilation errors${NC}"
else
    echo -e "${GREEN}✓ Application compiles successfully${NC}"
fi
echo ""

# Check 3: Configuration Files
echo "Check 3: Configuration Files"
echo "============================="
files=(
    "pom.xml"
    "src/main/resources/logback-spring.xml"
    "src/main/resources/application.properties"
    "src/main/java/com/example/splunk/util/SplunkHealthCheck.java"
)

for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓ $file exists${NC}"
    else
        echo -e "${RED}✗ $file NOT FOUND${NC}"
    fi
done
echo ""

# Check 4: HEC Token Configuration
echo "Check 4: HEC Token Configuration"
echo "=================================="
if grep -q "9d6dc1d9-6cc3-49b1-a8c9-0fe52832a12b" src/main/resources/application.properties; then
    echo -e "${GREEN}✓ HEC Token configured in application.properties${NC}"
else
    echo -e "${RED}✗ HEC Token NOT configured${NC}"
fi

if grep -q "9d6dc1d9-6cc3-49b1-a8c9-0fe52832a12b" src/main/resources/logback-spring.xml; then
    echo -e "${GREEN}✓ HEC Token configured in logback-spring.xml${NC}"
else
    echo -e "${RED}✗ HEC Token NOT configured${NC}"
fi
echo ""

# Check 5: Splunk Library Dependency
echo "Check 5: Splunk Library Dependency"
echo "==================================="
if grep -q "splunk-library-javalogging" pom.xml; then
    echo -e "${GREEN}✓ Splunk library declared in pom.xml${NC}"
else
    echo -e "${RED}✗ Splunk library NOT declared${NC}"
fi
echo ""

# Check 6: Logging Configuration
echo "Check 6: Logging Configuration"
echo "==============================="
if grep -q "logging.level.com.example.splunk" src/main/resources/application.properties; then
    echo -e "${GREEN}✓ Application logging level configured${NC}"
else
    echo -e "${YELLOW}⚠ Application logging level not configured (optional)${NC}"
fi

if grep -q "logging.level.com.splunk.logging" src/main/resources/application.properties; then
    echo -e "${GREEN}✓ Splunk logging level configured${NC}"
else
    echo -e "${YELLOW}⚠ Splunk logging level not configured (optional)${NC}"
fi
echo ""

echo "========================================"
echo "Verification Complete!"
echo "========================================"
echo ""
echo "Next Steps:"
echo "1. Start the application: mvn spring-boot:run"
echo "2. Check for 'Successfully connected to Splunk HEC' message"
echo "3. Make API calls: curl http://localhost:8010/api/employees"
echo "4. Search in Splunk: index=main sourcetype=springboot-logs"
echo ""
echo "For detailed troubleshooting, see: SPLUNK_TROUBLESHOOTING.md"

