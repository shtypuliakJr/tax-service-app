SELECT COUNT(*) totalReportCount,
       SUM(status = 'PROCESSING') Processing,
       SUM(status = 'APPROVED') Approved,
       SUM(status = 'DISAPPROVED') Disapproved
FROM report;

