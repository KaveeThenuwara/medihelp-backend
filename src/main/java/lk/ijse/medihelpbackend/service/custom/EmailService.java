package lk.ijse.medihelpbackend.service.custom;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.medihelpbackend.Entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private void sendHtml(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress(from, "MediHelp"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public String sendVerificationCode(String toEmail) {
        String code = String.format("%06d", new Random().nextInt(999999));

        // Split code into pairs for display
        String c1 = code.substring(0, 2);
        String c2 = code.substring(2, 4);
        String c3 = code.substring(4, 6);

        String html = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8"/>
          <meta name="viewport" content="width=device-width,initial-scale=1.0"/>
          <title>Verify Your Email – MediHelp</title>
        </head>
        <body style="margin:0;padding:0;background:#f0f4ff;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Helvetica Neue',sans-serif;">

          <table width="100%%" cellpadding="0" cellspacing="0" role="presentation" style="background:#f0f4ff;padding:48px 16px;">
            <tr><td align="center">

              <!-- Card -->
              <table width="560" cellpadding="0" cellspacing="0" role="presentation"
                     style="background:#ffffff;border-radius:28px;overflow:hidden;box-shadow:0 8px 48px rgba(29,78,216,0.10);">

                <!-- Top accent bar -->
                <tr>
                  <td style="height:6px;background:linear-gradient(90deg,#1d4ed8,#60a5fa,#818cf8);"></td>
                </tr>

                <!-- Header -->
                <tr>
                  <td style="padding:40px 48px 0;text-align:center;">
                    <div style="display:inline-flex;align-items:center;gap:10px;background:#eff6ff;border-radius:50px;padding:10px 22px;margin-bottom:32px;">
                      <span style="font-size:20px;">⚕</span>
                      <span style="font-size:16px;font-weight:800;color:#1d4ed8;letter-spacing:-0.3px;">MediHelp</span>
                    </div>
                    <h1 style="margin:0 0 8px;font-size:26px;font-weight:800;color:#0f172a;letter-spacing:-0.5px;">Verify your email</h1>
                    <p style="margin:0;color:#64748b;font-size:15px;line-height:1.6;">Enter the code below to complete your registration.</p>
                  </td>
                </tr>

                <!-- OTP Boxes -->
                <tr>
                  <td style="padding:36px 48px;">
                    <table width="100%%" cellpadding="0" cellspacing="0" role="presentation">
                      <tr>
                        <td align="center">
                          <div style="display:inline-flex;gap:12px;align-items:center;">

                            <!-- Box group 1 -->
                            <div style="display:inline-flex;gap:8px;">
                              <div style="width:52px;height:64px;background:#f8faff;border:2px solid #bfdbfe;border-radius:14px;display:flex;align-items:center;justify-content:center;font-size:28px;font-weight:900;color:#1d4ed8;font-family:'Courier New',monospace;text-align:center;line-height:64px;">%s</div>
                              <div style="width:52px;height:64px;background:#f8faff;border:2px solid #bfdbfe;border-radius:14px;display:flex;align-items:center;justify-content:center;font-size:28px;font-weight:900;color:#1d4ed8;font-family:'Courier New',monospace;text-align:center;line-height:64px;">%s</div>
                            </div>

                            <!-- Dot separator -->
                            <span style="font-size:22px;color:#cbd5e1;font-weight:900;line-height:1;">·</span>

                            <!-- Box group 2 -->
                            <div style="display:inline-flex;gap:8px;">
                              <div style="width:52px;height:64px;background:#f8faff;border:2px solid #bfdbfe;border-radius:14px;display:flex;align-items:center;justify-content:center;font-size:28px;font-weight:900;color:#1d4ed8;font-family:'Courier New',monospace;text-align:center;line-height:64px;">%s</div>
                              <div style="width:52px;height:64px;background:#f8faff;border:2px solid #bfdbfe;border-radius:14px;display:flex;align-items:center;justify-content:center;font-size:28px;font-weight:900;color:#1d4ed8;font-family:'Courier New',monospace;text-align:center;line-height:64px;">%s</div>
                            </div>

                            <!-- Dot separator -->
                            <span style="font-size:22px;color:#cbd5e1;font-weight:900;line-height:1;">·</span>

                            <!-- Box group 3 -->
                            <div style="display:inline-flex;gap:8px;">
                              <div style="width:52px;height:64px;background:#f8faff;border:2px solid #bfdbfe;border-radius:14px;display:flex;align-items:center;justify-content:center;font-size:28px;font-weight:900;color:#1d4ed8;font-family:'Courier New',monospace;text-align:center;line-height:64px;">%s</div>
                              <div style="width:52px;height:64px;background:#f8faff;border:2px solid #bfdbfe;border-radius:14px;display:flex;align-items:center;justify-content:center;font-size:28px;font-weight:900;color:#1d4ed8;font-family:'Courier New',monospace;text-align:center;line-height:64px;">%s</div>
                            </div>
                          </div>
                        </td>
                      </tr>
                    </table>

                    <!-- Expiry -->
                    <p style="text-align:center;margin:24px 0 0;font-size:13px;color:#94a3b8;">
                      ⏱ Expires in <strong style="color:#0f172a;">10 minutes</strong>
                    </p>
                  </td>
                </tr>

                <!-- Divider -->
                <tr><td style="padding:0 48px;"><div style="height:1px;background:#f1f5f9;"></div></td></tr>

                <!-- Security note -->
                <tr>
                  <td style="padding:24px 48px;">
                    <table cellpadding="0" cellspacing="0" role="presentation"
                           style="background:#f8fafc;border-radius:16px;padding:18px 22px;width:100%%;">
                      <tr>
                        <td style="width:36px;vertical-align:top;font-size:20px;padding-right:12px;">🔒</td>
                        <td style="font-size:13px;color:#475569;line-height:1.6;">
                          <strong style="color:#0f172a;">Security notice:</strong> MediHelp will never ask for this code via phone or chat. Do not share it with anyone.
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>

                <!-- Footer -->
                <tr>
                  <td style="padding:0 48px 36px;text-align:center;">
                    <p style="margin:0;color:#94a3b8;font-size:12px;line-height:1.8;">
                      If you did not create a MediHelp account, please ignore this email.<br>
                      © 2025 MediHelp · All rights reserved.
                    </p>
                  </td>
                </tr>

                <!-- Bottom accent bar -->
                <tr>
                  <td style="height:6px;background:linear-gradient(90deg,#818cf8,#60a5fa,#1d4ed8);"></td>
                </tr>

              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(
            String.valueOf(code.charAt(0)), String.valueOf(code.charAt(1)),
            String.valueOf(code.charAt(2)), String.valueOf(code.charAt(3)),
            String.valueOf(code.charAt(4)), String.valueOf(code.charAt(5))
        );

        sendHtml(toEmail, "MediHelp — Your Verification Code", html);
        return code;
    }

    public void sendPasswordResetCode(String toEmail, String code) {
        String html = """
        <!DOCTYPE html>
        <html lang="en">
        <head><meta charset="UTF-8"/><meta name="viewport" content="width=device-width,initial-scale=1.0"/></head>
        <body style="margin:0;padding:0;background:#fff8f0;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="padding:48px 16px;">
            <tr><td align="center">
              <table width="560" cellpadding="0" cellspacing="0"
                     style="background:#ffffff;border-radius:28px;overflow:hidden;box-shadow:0 8px 48px rgba(245,158,11,0.10);">
                <tr><td style="height:6px;background:linear-gradient(90deg,#d97706,#f59e0b,#fcd34d);"></td></tr>
                <tr>
                  <td style="padding:40px 48px 0;text-align:center;">
                    <div style="display:inline-flex;align-items:center;gap:10px;background:#fffbeb;border-radius:50px;padding:10px 22px;margin-bottom:32px;">
                      <span style="font-size:20px;">🔑</span>
                      <span style="font-size:16px;font-weight:800;color:#d97706;">MediHelp</span>
                    </div>
                    <h1 style="margin:0 0 8px;font-size:26px;font-weight:800;color:#0f172a;">Reset your password</h1>
                    <p style="margin:0;color:#64748b;font-size:15px;">Use the code below to create a new password.</p>
                  </td>
                </tr>
                <tr>
                  <td style="padding:36px 48px;text-align:center;">
                    <div style="display:inline-block;background:#fffbeb;border:2px solid #fde68a;border-radius:20px;padding:24px 48px;">
                      <p style="margin:0 0 6px;font-size:11px;font-weight:700;letter-spacing:3px;color:#d97706;text-transform:uppercase;">Reset Code</p>
                      <p style="margin:0;font-size:42px;font-weight:900;color:#b45309;letter-spacing:10px;font-family:'Courier New',monospace;">%s</p>
                    </div>
                    <p style="margin:16px 0 0;font-size:13px;color:#94a3b8;">⏱ Valid for <strong style="color:#0f172a;">10 minutes</strong></p>
                  </td>
                </tr>
                <tr><td style="padding:0 48px;"><div style="height:1px;background:#f1f5f9;"></div></td></tr>
                <tr>
                  <td style="padding:24px 48px;">
                    <table cellpadding="0" cellspacing="0" style="background:#fff8f0;border-radius:16px;padding:18px 22px;width:100%%;">
                      <tr>
                        <td style="width:36px;vertical-align:top;font-size:20px;padding-right:12px;">⚠️</td>
                        <td style="font-size:13px;color:#475569;line-height:1.6;"><strong style="color:#0f172a;">Didn't request this?</strong> Ignore this email — your password will remain unchanged.</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr><td style="padding:0 48px 36px;text-align:center;"><p style="margin:0;color:#94a3b8;font-size:12px;">© 2025 MediHelp · All rights reserved.</p></td></tr>
                <tr><td style="height:6px;background:linear-gradient(90deg,#fcd34d,#f59e0b,#d97706);"></td></tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(code);

        sendHtml(toEmail, "MediHelp — Password Reset Code", html);
    }

    public void sendWelcomeEmail(String toEmail, String name) {
        String html = """
        <!DOCTYPE html>
        <html lang="en">
        <head><meta charset="UTF-8"/><meta name="viewport" content="width=device-width,initial-scale=1.0"/></head>
        <body style="margin:0;padding:0;background:#f0fff4;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="padding:48px 16px;">
            <tr><td align="center">
              <table width="560" cellpadding="0" cellspacing="0"
                     style="background:#ffffff;border-radius:28px;overflow:hidden;box-shadow:0 8px 48px rgba(16,185,129,0.10);">
                <tr><td style="height:6px;background:linear-gradient(90deg,#059669,#10b981,#6ee7b7);"></td></tr>
                <tr>
                  <td style="padding:48px 48px 0;text-align:center;">
                    <div style="display:inline-flex;align-items:center;gap:10px;background:#ecfdf5;border-radius:50px;padding:10px 22px;margin-bottom:24px;">
                      <span style="font-size:20px;">⚕</span>
                      <span style="font-size:16px;font-weight:800;color:#059669;">MediHelp</span>
                    </div>
                    <div style="font-size:44px;margin-bottom:16px;">👋</div>
                    <h1 style="margin:0 0 8px;font-size:26px;font-weight:800;color:#0f172a;">Welcome, %s!</h1>
                    <p style="margin:0 0 32px;color:#64748b;font-size:15px;line-height:1.6;">Your account has been successfully verified. You're ready to start managing your healthcare.</p>
                  </td>
                </tr>
                <tr>
                  <td style="padding:0 48px 36px;">
                    <div style="background:#f8fafc;border-radius:20px;overflow:hidden;">
                      <div style="padding:18px 24px;border-bottom:1px solid #f1f5f9;display:flex;align-items:center;gap:16px;">
                        <span style="font-size:24px;">📅</span>
                        <div><div style="font-weight:700;color:#0f172a;font-size:14px;">Book Appointments</div><div style="color:#64748b;font-size:13px;margin-top:2px;">Browse certified doctors and schedule instantly.</div></div>
                      </div>
                      <div style="padding:18px 24px;border-bottom:1px solid #f1f5f9;display:flex;align-items:center;gap:16px;">
                        <span style="font-size:24px;">🔒</span>
                        <div><div style="font-weight:700;color:#0f172a;font-size:14px;">Secure Health Records</div><div style="color:#64748b;font-size:13px;margin-top:2px;">Your data is private and encrypted at rest.</div></div>
                      </div>
                      <div style="padding:18px 24px;display:flex;align-items:center;gap:16px;">
                        <span style="font-size:24px;">💊</span>
                        <div><div style="font-weight:700;color:#0f172a;font-size:14px;">Track Your Health</div><div style="color:#64748b;font-size:13px;margin-top:2px;">View history, manage payments, and more.</div></div>
                      </div>
                    </div>
                    <div style="text-align:center;margin-top:32px;">
                      <a href="http://localhost:3000/login"
                         style="display:inline-block;background:linear-gradient(135deg,#059669,#10b981);color:#ffffff;text-decoration:none;padding:16px 48px;border-radius:16px;font-weight:800;font-size:15px;letter-spacing:-0.3px;">
                        Go to Dashboard →
                      </a>
                    </div>
                  </td>
                </tr>
                <tr><td style="padding:0 48px 36px;text-align:center;"><p style="margin:0;color:#94a3b8;font-size:12px;">© 2025 MediHelp · All rights reserved.</p></td></tr>
                <tr><td style="height:6px;background:linear-gradient(90deg,#6ee7b7,#10b981,#059669);"></td></tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(name);

        sendHtml(toEmail, "Welcome to MediHelp 🎉", html);
    }

    public void sendAppointmentConfirmation(Appointment a) {
        String patientEmail = a.getPatient().getEmail();
        String doctorName = a.getDoctor().getUser().getName();
        if (doctorName == null || doctorName.isBlank()) doctorName = a.getDoctor().getUser().getEmail();
        
        String specialization = a.getDoctor().getSpecialization();
        if (specialization == null) specialization = "General Care";
        
        String hospital = a.getDoctor().getHospital();
        if (hospital == null) hospital = "MediHelp Medical Center";
        
        String date = a.getAppointmentDate().toString();
        String status = a.getStatus();
        String idSnippet = a.getAppointmentId().toString().substring(0, 8);

        String statusColor = status.equals("CONFIRMED") ? "#10b981" : status.equals("PENDING") ? "#f59e0b" : "#3b82f6";

        String html = """
        <!DOCTYPE html>
        <html lang="en">
        <head><meta charset="UTF-8"/><meta name="viewport" content="width=device-width,initial-scale=1.0"/></head>
        <body style="margin:0;padding:0;background:#f8fafc;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="padding:48px 16px;">
            <tr><td align="center">
              <table width="560" cellpadding="0" cellspacing="0"
                     style="background:#ffffff;border-radius:28px;overflow:hidden;box-shadow:0 8px 48px rgba(0,0,0,0.05);">
                <tr><td style="height:6px;background:linear-gradient(90deg,#3b82f6,#60a5fa,#93c5fd);"></td></tr>
                <tr>
                  <td style="padding:40px 48px 0;text-align:center;">
                    <div style="display:inline-flex;align-items:center;gap:10px;background:#eff6ff;border-radius:50px;padding:10px 22px;margin-bottom:24px;">
                      <span style="font-size:20px;">📅</span>
                      <span style="font-size:16px;font-weight:800;color:#2563eb;">Appointment Detail</span>
                    </div>
                    <h1 style="margin:0 0 8px;font-size:24px;font-weight:800;color:#0f172a;">Your appointment is %s</h1>
                    <p style="margin:0;color:#64748b;font-size:15px;">Appointment Ref: <strong style="color:#0f172a;">#%s</strong></p>
                  </td>
                </tr>
                <tr>
                  <td style="padding:32px 48px;">
                    <div style="background:#f1f5f9;border-radius:24px;padding:32px;border:1px solid #e2e8f0;">
                      <div style="margin-bottom:24px;">
                        <p style="margin:0 0 4px;font-size:11px;font-weight:700;color:#94a3b8;text-transform:uppercase;">Doctor</p>
                        <p style="margin:0;font-size:18px;font-weight:800;color:#0f172a;">Dr. %s</p>
                        <p style="margin:2px 0 0;font-size:14px;color:#3b82f6;font-weight:600;">%s Specialist</p>
                      </div>
                      <div style="margin-bottom:24px;">
                        <p style="margin:0 0 4px;font-size:11px;font-weight:700;color:#94a3b8;text-transform:uppercase;">Hospital / Location</p>
                        <p style="margin:0;font-size:16px;font-weight:700;color:#0f172a;">%s</p>
                      </div>
                      <div style="display:flex;gap:32px;">
                        <div style="flex:1;">
                          <p style="margin:0 0 4px;font-size:11px;font-weight:700;color:#94a3b8;text-transform:uppercase;">Date</p>
                          <p style="margin:0;font-size:16px;font-weight:700;color:#0f172a;">%s</p>
                        </div>
                        <div style="flex:1;">
                          <p style="margin:0 0 4px;font-size:11px;font-weight:700;color:#94a3b8;text-transform:uppercase;">Status</p>
                          <p style="margin:0;font-size:14px;font-weight:800;color:%s;">%s</p>
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
                <tr><td style="padding:0 48px 36px;text-align:center;"><p style="margin:0;color:#94a3b8;font-size:12px;">Thank you for choosing MediHelp for your healthcare needs.</p></td></tr>
                <tr><td style="height:6px;background:linear-gradient(90deg,#93c5fd,#60a5fa,#3b82f6);"></td></tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(status.toLowerCase(), idSnippet, doctorName, specialization, hospital, date, statusColor, status);

        sendHtml(patientEmail, "MediHelp Appointment Detail — #" + idSnippet, html);
    }
}
